package org.bytecamp.program_repair.astor_plugin.services

import com.google.protobuf.ByteString
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
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

            val grpcStub = RepairServerGrpc.newBlockingStub(channel)
            val builder = RepairTaskRequest.newBuilder().setProject(project.name)
            if (host == "localhost") {
                builder
                    .setLocationType(RepairTaskRequest.LocationType.PATH)
                    .setLocation(projectBase)

            } else {
                val out = ByteArrayOutputStream()
                val files = ArrayList<File>()
                includeFiles(File(projectBase), files)
                ZipManager.zip(File(projectBase), files, out)
                builder
                    .setLocationType(RepairTaskRequest.LocationType.ZIP)
                    .setContent(ByteString.copyFrom(out.toByteArray()))
            }

            val request = builder.setAlgorithm(settings.algorithm)
                .build()

            val request2 = builder.setContent(ByteString.EMPTY)
                .build()
            window.appendText("\nRequesting with args $request2\n")

            var results: List<RepairTaskResult>? = null
            for (resp in grpcStub.submitTask(request)) {
                when (resp.frameType) {
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

            lastResults = results
            window.appendText("Astor results: $results\n")
            if (results == null) {
                AstorNotification.getNotificationGroup()
                    .createNotification(
                        "Astor execution finished without result response",
                        NotificationType.ERROR
                    )
                    .notify(project)
            } else {
                if (results.find { x -> x.success } != null) {
                    AstorNotification.getNotificationGroup()
                        .createNotification("Astor execution finished with solutions", NotificationType.INFORMATION)
                        .notify(project)
                } else if (results.isNotEmpty()) {
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
