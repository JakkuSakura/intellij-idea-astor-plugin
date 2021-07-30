package org.bytecamp.program_repair.astor_plugin.window

import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTextPane


class AstorOutput: JPanel() {
    var text: JTextPane = JTextPane()
    var jsp = com.intellij.ui.components.JBScrollPane(text)

    init {
        layout = BorderLayout()
        text.isEditable = false
        super.add(jsp)
    }
    fun appendText(str: String) {
        val document = text.document
        document.insertString(document.length, str, null)
        text.caretPosition = document.length
    }

}