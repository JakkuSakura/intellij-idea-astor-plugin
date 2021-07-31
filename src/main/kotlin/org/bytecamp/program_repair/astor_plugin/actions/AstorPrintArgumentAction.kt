package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.util.castSafelyTo
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService
import org.bytecamp.program_repair.astor_plugin.window.AstorOutput
import org.bytecamp.program_repair.astor_plugin.window.AstorWindowFactory

class AstorPrintArgumentAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val service = e.project?.service<AstorProjectService>()!!
        if (e.project != null) {
            AstorWindowFactory.getAstorOutput(e.project!!)!!.appendText("To execute astor with arguments \n" + service.getConfig().toArgs().joinToString("\n"))
        }
    }
}