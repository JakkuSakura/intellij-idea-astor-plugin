package org.bytecamp.program_repair.astor_plugin.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService

internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<AstorProjectService>()
    }
}
