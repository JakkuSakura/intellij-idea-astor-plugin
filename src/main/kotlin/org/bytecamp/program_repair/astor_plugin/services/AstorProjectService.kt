package org.bytecamp.program_repair.astor_plugin.services

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
import org.bytecamp.program_repair.astor_plugin.configs.AstorInputConfig
import org.bytecamp.program_repair.astor_plugin.window.AstorWindowFactory
import java.io.File


class AstorProjectService(val project: Project) {
    private val logger = com.intellij.openapi.diagnostic.Logger.getInstance(AstorProjectService::class.java)
    private val channel: ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 10000)
        .usePlaintext()
        .build()

    private val grpcStub: AstorLanguageServerGrpc.AstorLanguageServerBlockingStub =
        AstorLanguageServerGrpc.newBlockingStub(channel)
    val window = AstorWindowFactory.getAstorOutput(project)!!

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

    fun getConfig(): AstorInputConfig {
        val config = AstorInputConfig()
        config.location = project.basePath!!
        config.projectName = project.basePath?.split(File.separator)?.last()!!
        val modules = ModuleManager.getInstance(project).modules

        for (module in modules) {
            for (srcRoot in ModuleRootManager.getInstance(module).sourceRoots) {
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
                config.out = "$base/build/astor"
            }
            "maven" -> {
                config.bin = "$base/target/classes"
                config.binTest = "$base/target/test-classes"
                config.out = "$base/target/astor"
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
        val result = StringBuilder()
        window.appendText("\nRequesting with args $config\n")

        for (resp in grpcStub.execute(args)) {
            when (resp.frameType) {
                ExecuteResponse.FrameType.RESULT -> {
                    result.append(resp.arg)
                }
                ExecuteResponse.FrameType.STDOUT -> {
                    window.appendText(resp.arg)
                }
                else -> {
                    throw RuntimeException("Unreachable")
                }
            }
        }
        window.appendText("Result: $result\n")
        return result.toString()
    }
}
