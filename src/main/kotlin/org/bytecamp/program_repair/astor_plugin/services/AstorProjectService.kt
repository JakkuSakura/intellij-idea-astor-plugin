package org.bytecamp.program_repair.astor_plugin.services

import com.google.protobuf.ByteString
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.internal.synchronized
import org.bytecamp.program_repair.astor_plugin.configs.AppSettingsState
import org.bytecamp.program_repair.astor_plugin.utils.ZipManager
import org.bytecamp.program_repair.astor_plugin.window.AstorNotification
import org.bytecamp.program_repair.astor_plugin.window.AstorWindowFactory
import org.bytecamp.program_repair.backend.grpc.RepairServerGrpc
import org.bytecamp.program_repair.backend.grpc.RepairTaskRequest
import org.bytecamp.program_repair.backend.grpc.RepairTaskResponse
import org.bytecamp.program_repair.backend.grpc.RepairTaskResult
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.ceil
import kotlin.math.min

class AstorProjectService(val project: Project) {
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance(AstorProjectService::class.java)

    var lastResults: List<RepairTaskResult>? = null
    val window = AstorWindowFactory.getAstorOutput(project)!!
    private fun includeFiles(node: File, collector: ArrayList<File>) {
        if (node.name.startsWith(".")) return
        if (node.isFile) {
            collector.add(node)
        } else if (node.isDirectory) {
            for (child in node.listFiles())
                includeFiles(child, collector)
        }
    }

    @Synchronized
    fun execute(): List<RepairTaskResult>? {

        window.clear()
        lastResults = null
        try {
            val projectBase = project.basePath!!
            val settings = AppSettingsState.instance
            val spt = settings.backendAddress.split(":")
            val host = spt[0]
            val port = spt[1].toInt()
            val channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build()

            val grpcStub = RepairServerGrpc.newStub(channel)
            val builder = RepairTaskRequest.newBuilder()
                .setProject(project.name)
                .setAlgorithm(settings.algorithm)
            if (host == "localhost") {
                builder
                    .setLocationType(RepairTaskRequest.LocationType.PATH)
                    .setLocation(projectBase)

            } else {
                builder
                    .setLocationType(RepairTaskRequest.LocationType.ZIP)
            }


            var results: List<RepairTaskResult>? = null

            class Task : StreamObserver<RepairTaskResponse> {
                var err: Throwable? = null
                var done = false
                override fun onNext(resp: RepairTaskResponse?) {
                    when (resp!!.frameType) {
                        RepairTaskResponse.FrameType.RESULT -> {
                            window.appendText(resp.message)
                            results = resp.resultList
                        }
                        RepairTaskResponse.FrameType.STDOUT -> {
                            window.appendText(resp.message)
                        }
                        else -> {
                            throw RuntimeException("Unreachable")
                        }
                    }
                }

                override fun onError(p0: Throwable?) {
                    err = p0
                    done = true
                }

                override fun onCompleted() {
                    done = true
                }
            }

            val task = Task()
            val streaming = grpcStub.submitTask(task)
            when (builder.locationType) {
                RepairTaskRequest.LocationType.PATH -> {
                    val request = builder.build()
                    window.appendText("\nRequesting with args $request\n")
                    streaming.onNext(request)
                }
                RepairTaskRequest.LocationType.ZIP -> {
                    val request = builder.build()
                    window.appendText("\nRequesting with args $request\n")

                    val out = ByteArrayOutputStream()
                    val files = ArrayList<File>()
                    includeFiles(File(projectBase), files)
                    ZipManager.zip(File(projectBase), files, out)

                    val chunkSize = 1024 * 512
                    val maxIteration = ceil(out.size().toDouble() / chunkSize.toDouble()).toInt()
                    val bytes = ByteString.copyFrom(out.toByteArray())
                    for (i in 0 until maxIteration) {
                        builder.contentContinue = i != maxIteration - 1
                        builder.content = bytes.substring(i * chunkSize, min(bytes.size(), (i + 1) * chunkSize))
                        streaming.onNext(builder.build())
                    }

                }
                else -> {
                    throw RuntimeException("Unreachable")
                }
            }

            streaming.onCompleted()
            while (!task.done) {
                Thread.sleep(100)
            }
            lastResults = results
            window.appendText("Astor results: $lastResults\n")
            if (lastResults == null) {
                AstorNotification.getNotificationGroup()
                    .createNotification(
                        "Astor execution finished without result response",
                        NotificationType.ERROR
                    )
                    .notify(project)
            } else {
                val lastResults = lastResults!!
                if (lastResults.find { x -> x.success } != null) {
                    AstorNotification.getNotificationGroup()
                        .createNotification("Astor execution finished with solutions", NotificationType.INFORMATION)
                        .notify(project)
                } else if (lastResults.isNotEmpty()) {
                    AstorNotification.getNotificationGroup()
                        .createNotification(
                            "Astor could not find solutions",
                            NotificationType.WARNING
                        )
                        .notify(project)
                } else {
                    AstorNotification.getNotificationGroup()
                        .createNotification(
                            "Nothing need fixing according astor",
                            NotificationType.INFORMATION
                        )
                        .notify(project)
                }
            }

            return results
        } catch (ex: Exception) {
            window.appendText(ex.stackTraceToString())
            AstorNotification.getNotificationGroup()
                .createNotification("Astor execution error: $ex", NotificationType.ERROR)
                .notify(project)
            logger.error(ex)
        }
        return null


    }
}
