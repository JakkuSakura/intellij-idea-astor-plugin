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
import kotlin.math.log


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
    var pkg: String = ""
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
    val logger = com.intellij.openapi.diagnostic.Logger.getInstance("AstorProjectService")

    fun getCommonPackage(file: VirtualFile, prefix: String): String {
        return if (file.isDirectory) {
            if (file.children.size > 1) {
                if (prefix.isEmpty()) {
                    file.name
                } else {
                    prefix + "." + file.name
                }
            } else if (file.children.isEmpty()) {
                prefix
            } else {
                if (file.children[0].name == "java") {
                    getCommonPackage(file.children[0], prefix)
                } else {
                    getCommonPackage(file.children[0], prefix + "." + file.name)
                }
            }
        } else {
            prefix
        }
    }
    fun getBuildSystem(): String {
        val base = project.projectFile?.parent?.parent
        val system =  if (base?.findChild("build.gradle") != null || base?.findChild("build.gradle.kts") != null)  {
            "gradle"
        } else if (base?.findChild("pom.xml") != null) {
            "maven"
        } else {
            "others"
        }
        logger.info("Base " + base?.path + " build system " + system)
        return system
    }

    fun getConfig(): AstorConfig {
        val config = AstorConfig()
        config.location = project.basePath!!
        val modules = ModuleManager.getInstance(project).modules

        for (module in modules) {
            logger.info("Module %s path %s".format(module.name, module.moduleFilePath))
            for (srcRoot in ModuleRootManager.getInstance(module).sourceRoots) {
                logger.info("Source root " + srcRoot.path)
                if (srcRoot.name == "java") {
                    if (srcRoot.path.contains("main")) {
                        config.src = srcRoot.path
                        config.pkg = getCommonPackage(srcRoot, "")
                    } else if (srcRoot.path.contains("test")) {
                        config.srcTest = srcRoot.path
                    }
                }
            }
        }
        when (getBuildSystem()) {
            "gradle" -> {
                config.bin = project.projectFile!!.parent.path + "build/classes/java/main"
                config.binTest = project.projectFile!!.parent.path + "build/classes/java/test"
            }
            "maven" -> {
                config.bin = project.projectFile!!.parent.path + "target/classes"
                config.binTest = project.projectFile!!.parent.path + "target/test-classes"
            }
            else -> {
                Messages.showErrorDialog(project, "Does not support build systems other than gradle or maven", "Astor")
            }
        }
        return config
    }

    fun execute() {
        val config = getConfig()
        main.execute(config.toArgs())
    }
}
