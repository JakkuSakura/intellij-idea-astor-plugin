package org.bytecamp.program_repair.astor_plugin.services

import com.intellij.openapi.project.Project
import org.bytecamp.program_repair.astor_plugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
