package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.lang.java.JavaLanguage
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.CurrentContentRevision
import com.intellij.openapi.vcs.changes.actions.diff.ShowDiffAction
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.vcsUtil.VcsUtil
import org.bytecamp.program_repair.astor_plugin.code.CodeImporter
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService
import org.bytecamp.program_repair.backend.grpc.RepairTaskResult
import java.io.File


object AstorDiff {
    fun getBuildSystem(source: String): String {
        val system = if (File(source, "build.gradle").exists() || File(source, "build.gradle.kts").exists()) {
            "gradle"
        } else if (File(source, "pom.xml").exists()) {
            "maven"
        } else {
            "others"
        }
        return system
    }

    fun getTargetPath(source: String): String {
        when (getBuildSystem(source)) {
            "gradle" -> {
                return File(source, "build").path
            }
            "maven" -> {
                return File(source, "target").path
            }
            else -> {
                throw IllegalStateException("Does not support other build systems")
            }
        }
    }

    fun showDiff(project: Project, patches: List<RepairTaskResult.Patch>) {
        try {
            val changes = ArrayList<Change>()
            val psiFactory = PsiFileFactory.getInstance(project)
            val target = File(getTargetPath(project.basePath!!), "astor")
            target.mkdirs()
            for (chunk in patches) {
                val sourcePath = File(project.basePath, chunk.sourcePath).path
                val srcCode = psiFactory.createFileFromText(
                    sourcePath,
                    JavaLanguage.INSTANCE,
                    File(sourcePath).readText(Charsets.UTF_8)
                )
                val modifiedPath = File(target, chunk.sourcePath).path
                var modified = psiFactory
                    .createFileFromText(
                        modifiedPath,
                        JavaLanguage.INSTANCE,
                        chunk.modified
                    )

                JavaCodeStyleManager.getInstance(project).shortenClassReferences(modified)
                CodeStyleManager.getInstance(project)
                    .reformatText(modified, modified.textRange.startOffset, modified.textRange.endOffset)
                modified = CodeImporter().importCode(srcCode, modified)
                val modifiedFile = File(modifiedPath)
                File(modifiedFile.parent).mkdirs()
                modifiedFile.writeText(modified.text)

                changes.add(createChangeVsLocal(sourcePath, modifiedPath))
            }


            VfsUtil.findFileByIoFile(target, true)?.refresh(false, true)
            ShowDiffAction.showDiffForChange(project, changes)

        } catch (ex: Exception) {
            val service = project.service<AstorProjectService>()
            service.window.appendText(ex.stackTraceToString())
        }
    }

    fun createChangeVsLocal(filePath: String, modified: String): Change {
        val beforeVirtualFile = CurrentContentRevision.create(VcsUtil.getFilePath(filePath))
        val afterVirtualFile = CurrentContentRevision.create(VcsUtil.getFilePath(modified))
        return Change(beforeVirtualFile, afterVirtualFile)
    }
}

class AstorShowDiffAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val service = project.service<AstorProjectService>()
        if (service.lastResults != null && service.lastResults!![0].patchList.isNotEmpty()) {
            runWriteCommandAction(project) {
                AstorDiff.showDiff(project, service.lastResults!![0].patchList)
            }
        } else {
            NotificationGroupManager.getInstance().getNotificationGroup("AstorShowDiffNotificationGroup")
                .createNotification("You should run 'Execute Astor' before Show Diff", NotificationType.INFORMATION)
                .notify(project)
        }
    }
}