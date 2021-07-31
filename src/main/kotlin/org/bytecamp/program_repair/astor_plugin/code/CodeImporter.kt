package org.bytecamp.program_repair.astor_plugin.code

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiJavaParserFacade
import com.intellij.psi.impl.PsiJavaParserFacadeImpl
import com.jetbrains.rd.util.string.printToString

class CodeImporter(val project: Project) {
    /**
    modified:
    ```java
    int foo();
    ```
    src:
    ```java
    // This is comment
    void foo();
    ```

    result:
    ```java
    // This is comment
    int foo();
    ```

    It involves the following steps:
    - Build location references for both modified and src
    - Compare references and replace modified block to src

     */
    fun importComment(src: String, modified: String): String {
        val file = PsiFileFactory.getInstance(project).createFileFromText("test", JavaLanguage.INSTANCE, "class Foo() {}")
//        val srcRef = CodeReferencesBuilder(src).buildReferences()
//        val modifiedRef = CodeReferencesBuilder(modified).buildReferences()
        TODO()
    }
}