package org.bytecamp.program_repair.astor_plugin.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.bytecamp.program_repair.astor.grpc.AstorLanguageServerGrpc
import org.bytecamp.program_repair.astor.grpc.ExecuteRequest
import org.bytecamp.program_repair.astor.grpc.ExecuteResponse
import java.io.File


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
    private var normaized = false
    private fun normalize() {
        if (!normaized) {
            val location = File(this.location)
            src = File(src).relativeTo(location).path
            srcTest = File(srcTest).relativeTo(location).path
            bin = File(bin).relativeTo(location).path
            binTest = File(binTest).relativeTo(location).path
            normaized = true
        }
    }

    fun toArgs(): Array<String> {
        normalize()
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

class AstorProjectService(val project: Project) : Disposable {
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance("AstorProjectService")
    private val channel: ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 10000)
        .usePlaintext()
        .build()

    val grpcStub: AstorLanguageServerGrpc.AstorLanguageServerBlockingStub =
        AstorLanguageServerGrpc.newBlockingStub(channel)

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
                if (file.name == "java") {
                    getCommonPackage(file.children[0], prefix)
                } else if (prefix.isEmpty()) {
                    getCommonPackage(file.children[0], file.name)
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
        val system = if (base?.findChild("build.gradle") != null || base?.findChild("build.gradle.kts") != null) {
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

        val base = project.projectFile!!.parent!!.parent.path
        when (getBuildSystem()) {
            "gradle" -> {
                config.bin = "$base/build/classes/java/main"
                config.binTest = "$base/build/classes/java/test"
            }
            "maven" -> {
                config.bin = "$base/target/classes"
                config.binTest = "$base/target/test-classes"
            }
            else -> {
                Messages.showErrorDialog(project, "Does not support build systems other than gradle or maven", "Astor")
            }
        }
        return config
    }

    fun execute(): String {
        val config = getConfig()
        val args = ExecuteRequest.newBuilder()
            .addAllArgs(config.toArgs().asIterable())
            .build()
        val helloResponse: ExecuteResponse = grpcStub.execute(
            args
        )
        return helloResponse.arg.toString()
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}
