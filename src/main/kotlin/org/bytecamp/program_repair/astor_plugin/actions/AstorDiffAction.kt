package org.bytecamp.program_repair.astor_plugin.actions

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.psi.PsiFileFactory
import org.bytecamp.program_repair.astor_plugin.code.ReformatCode
import org.bytecamp.program_repair.astor_plugin.services.AstorProjectService
import java.io.File

class AstorDiffAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        val service = e.project?.service<AstorProjectService>()!!
        val outputConfig = service.getConfig().getOutConfig()
        for (patch in outputConfig.patches) {
            for (chunk in patch.patchhunks) {
                val srcCode = File(chunk.getModifiedFilePath()).readText(Charsets.UTF_8)
                val file = PsiFileFactory.getInstance(e.project!!)
                    .createFileFromText(chunk.getModifiedFilePath(), JavaLanguage.INSTANCE, srcCode)
                ReformatCode.reformatFile(e.project!!, file)
                File(chunk.getModifiedFilePath()).writeText(file.text)
            }
        }
    }
}