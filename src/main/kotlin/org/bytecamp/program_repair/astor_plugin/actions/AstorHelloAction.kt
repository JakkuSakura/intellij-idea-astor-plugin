package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class AstorHelloAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        Messages.showMessageDialog(e.project, "Hello", "Astor", null)
    }
}