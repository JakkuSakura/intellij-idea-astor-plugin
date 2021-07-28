package org.bytecamp.program_repair.astor_plugin.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.CompilerProjectExtension
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import fr.inria.main.evolution.AstorMain
import java.io.IOException


object Keys {
    const val MODE = "-mode"
    const val LOCATION = "-location"
    const val PACKAGE = "-package"
    const val SRC = "-srcjavafolder"
    const val SRC_TEST = "-srctestfolder"
    const val BIN = "-binjavafolder"
    const val BIN_TEST = "-bintestfolder"
}

class AstorConfig {
    var mode: String = "jGenProg"
    var pkg: String = "com.company"
    var location: String = ""
    var src: String = ""
    var srcTest: String = ""
    var bin: String = ""
    var binTest: String = ""
    fun toArgs(): Array<String> {
        return arrayOf(
            Keys.MODE,
            mode,
            Keys.PACKAGE,
            pkg,
            Keys.LOCATION,
            location,
            Keys.SRC,
            src,
            Keys.SRC_TEST,
            srcTest,
            Keys.BIN,
            bin,
            Keys.BIN_TEST,
            binTest
        )
    }
    override fun toString(): String {
        return toArgs().joinToString(" ")
    }
}

@Service
class AstorProjectService(val project: Project) {
    val main: AstorMain = AstorMain()

    fun getCommonPackage(file: VirtualFile, prefix: String): String {
        return if (file.isDirectory) {
            if (file.children.size > 1) {
                prefix + "." + file.name
            } else if (file.children.isEmpty()) {
                prefix
            } else {
                getCommonPackage(file, prefix + "." + file.name)
            }
        } else {
            throw IOException("Cannot getCommonPackage in file " + file.path)
        }
    }

    fun getConfig(): AstorConfig {
        val config = AstorConfig()
        config.location = project.basePath!!
        for (module in ModuleManager.getInstance(project).modules) {
            val srcRoots = ModuleRootManager.getInstance(module).sourceRoots
            if (srcRoots.size != 1) {
                Messages.showWarningDialog(project, "Can only process one source root", "Astor Warn")
            }
            if (module.name == "main") {
                config.src = srcRoots[0].path
                config.pkg = getCommonPackage(srcRoots[0], "")
            } else if (module.name == "test") {
                config.srcTest = srcRoots[0].path
            }

        }
        val compiler = CompilerProjectExtension.getInstance(project)!!

        config.bin =  compiler.compilerOutput?.path!!
        config.binTest = compiler.compilerOutput?.path!!

        return config
    }

    fun execute() {
        val config = getConfig()
        main.execute(config.toArgs())
    }
}
