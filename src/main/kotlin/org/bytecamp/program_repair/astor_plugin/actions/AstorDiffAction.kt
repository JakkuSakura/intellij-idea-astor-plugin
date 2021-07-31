package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.CurrentContentRevision
import com.intellij.openapi.vcs.changes.actions.diff.ShowDiffAction
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.vcsUtil.VcsUtil
import org.bytecamp.program_repair.astor_plugin.code.CodeImporter
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService
import java.io.File

object AstorDiff {
    fun showDiff(project: Project) {
        val service = project.service<AstorProjectService>()
        val outputConfig = service.getConfig().getOutConfig()
        val changes = ArrayList<Change>()
        val psiFactory = PsiFileFactory.getInstance(project)
        for (patch in outputConfig.patches) {
            for (chunk in patch.patchhunks) {
                val srcCode = psiFactory.createFileFromText(
                    chunk.getPath(),
                    JavaLanguage.INSTANCE,
                    File(chunk.getPath()).readText(Charsets.UTF_8)
                )
                var modified = psiFactory
                    .createFileFromText(
                        chunk.getModifiedFilePath(),
                        JavaLanguage.INSTANCE,
                        File(chunk.getModifiedFilePath()).readText(Charsets.UTF_8)
                    )

                JavaCodeStyleManager.getInstance(project).shortenClassReferences(modified)
                CodeStyleManager.getInstance(project)
                    .reformatText(modified, modified.textRange.startOffset, modified.textRange.endOffset)
                modified = CodeImporter().importCode(srcCode, modified)
                File(chunk.getModifiedFilePath()).writeText(modified.text)

                changes.add(createChangeVsLocal(chunk.getPath(), chunk.getModifiedFilePath()))
            }
        }

        ShowDiffAction.showDiffForChange(project, changes)
    }

    fun createChangeVsLocal(filePath: String, afterFilePath: String): Change {
        val beforeVirtualFile = CurrentContentRevision.create(VcsUtil.getFilePath(filePath))
        val afterVirtualFile = CurrentContentRevision.create(VcsUtil.getFilePath(afterFilePath))
        return Change(beforeVirtualFile, afterVirtualFile)
    }
}

class AstorDiffAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        runWriteCommandAction(project) {
            AstorDiff.showDiff(project)
        }
    }
}