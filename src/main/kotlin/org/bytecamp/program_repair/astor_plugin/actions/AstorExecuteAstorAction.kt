package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Messages
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService

class AstorExecuteAstorAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val service = e.project?.service<AstorProjectService>()!!
        Messages.showMessageDialog(e.project, "Executing astor with arguments \n" + service.getConfig().toArgs().joinToString("\n"), "Astor", null)
        val result = service.execute()
        Messages.showMessageDialog(e.project, "Executed astor: $result", "Astor", null)
    }
}