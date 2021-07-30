package org.bytecamp.program_repair.astor_plugin.code

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager


object ReformatCode {
    fun reformatFile(project: Project, file: PsiFile) {
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformatText(file, listOf(file.textRange))
        }
    }
}
