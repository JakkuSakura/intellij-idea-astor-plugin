package org.bytecamp.program_repair.astor_plugin.services

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.bytecamp.program_repair.astor_plugin.window.AstorWindowFactory
import org.bytecamp.program_repair.backend.grpc.RepairServerGrpc
import org.bytecamp.program_repair.backend.grpc.RepairTaskRequest
import org.bytecamp.program_repair.backend.grpc.RepairTaskResponse
import org.bytecamp.program_repair.backend.grpc.RepairTaskResult


class AstorProjectService(val project: Project) {
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance(AstorProjectService::class.java)
    private val channel: ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 10000)
        .usePlaintext()
        .build()

    private val grpcStub = RepairServerGrpc.newBlockingStub(channel)

    var lastResults: List<RepairTaskResult>? = null
    val window = AstorWindowFactory.getAstorOutput(project)!!

    fun execute(): List<RepairTaskResult>? {
        window.clear()
        val request = RepairTaskRequest.newBuilder()
            .setLocationType(RepairTaskRequest.LocationType.PATH)
            .setLocation(project.basePath)
            .setAlgorithm("jGenProg")
            .build()
        window.appendText("\nRequesting with args $request\n")
        try {
            var results: List<RepairTaskResult>? = null
            for (resp in grpcStub.submitTask(request)) {
                when (resp.frameType) {
                    RepairTaskResponse.FrameType.RESULT -> {
                        window.appendText(resp.message)
                        results = resp.resultList
                        lastResults = results
                    }
                    RepairTaskResponse.FrameType.STDOUT -> {
                        window.appendText(resp.message)
                    }
                    else -> {
                        throw RuntimeException("Unreachable")
                    }
                }
            }
            window.appendText("Result: $results\n")
            NotificationGroupManager.getInstance().getNotificationGroup("AstorDoneNotificationGroup")
                .createNotification("Astor execution finished", NotificationType.INFORMATION)
                .notify(project)
            return results
        } catch (ex: Exception) {
            window.appendText(ex.stackTraceToString())
            NotificationGroupManager.getInstance().getNotificationGroup("AstorDoneNotificationGroup")
                .createNotification("Astor execution error: $ex", NotificationType.ERROR)
                .notify(project)
            logger.error(ex)
        }
        return null
    }
}
