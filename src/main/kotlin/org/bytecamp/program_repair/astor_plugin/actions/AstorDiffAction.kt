package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.CurrentContentRevision
import com.intellij.openapi.vcs.changes.actions.diff.ShowDiffAction
import com.intellij.openapi.vcs.impl.VcsVirtualFileContentRevision
import com.intellij.openapi.vcs.vfs.VcsVirtualFile
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiFileFactory
import com.intellij.vcsUtil.VcsUtil
import org.bytecamp.program_repair.astor_plugin.code.ReformatCode
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService
import java.io.File

class AstorDiffAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        val service = e.project?.service<AstorProjectService>()!!
        val outputConfig = service.getConfig().getOutConfig()
        val changes = ArrayList<Change>()
        for (patch in outputConfig.patches) {
            for (chunk in patch.patchhunks) {
                val srcCode = File(chunk.getModifiedFilePath()).readText(Charsets.UTF_8)
                val file = PsiFileFactory.getInstance(e.project!!)
                    .createFileFromText(chunk.getModifiedFilePath(), JavaLanguage.INSTANCE, srcCode)
                ReformatCode.reformatFile(e.project!!, file)
                File(chunk.getModifiedFilePath()).writeText(file.text)
                changes.add(createChangeVsLocal(chunk.getPath(), chunk.getModifiedFilePath()))
            }
        }

        ShowDiffAction.showDiffForChange(e.project!!, changes)
    }

    private fun createChangeVsLocal(filePath: String, afterFilePath: String): Change {
        val beforeVirtualFile = CurrentContentRevision.create(VcsUtil.getFilePath(filePath))
        val afterVirtualFile = CurrentContentRevision.create(VcsUtil.getFilePath(afterFilePath))
        return Change(beforeVirtualFile, afterVirtualFile)
    }

}