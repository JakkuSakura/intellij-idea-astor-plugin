package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Messages
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService
import kotlin.concurrent.thread

class AstorExecuteAstorAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val service = project.service<AstorProjectService>()
        thread {
            service.execute()
            runWriteCommandAction(project) {
                AstorDiff.showDiff(project)
            }
        }
    }
}