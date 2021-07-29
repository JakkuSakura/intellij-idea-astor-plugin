package org.bytecamp.program_repair.astor_plugin.window

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.intellij.util.castSafelyTo

class AstorWindowFactory: ToolWindowFactory {
    companion object {
        fun getAstorOutput(project: Project): AstorOutput? {
            val window = ToolWindowManager.getInstance(project).getToolWindow("Astor")!!
            return window.contentManager.findContent(project.name).component.castSafelyTo<AstorOutput>()
        }
    }
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = AstorOutput()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(myToolWindow, project.name, false)
        toolWindow.contentManager.addContent(content)
    }
}