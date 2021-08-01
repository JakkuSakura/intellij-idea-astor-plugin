package org.bytecamp.program_repair.astor_plugin.window

import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.text.Document


class AstorOutput: JPanel() {
    var text = JTextArea()
    var jsp = com.intellij.ui.components.JBScrollPane(text)

    init {
        layout = BorderLayout()
        text.isEditable = false
        text.autoscrolls = true
        text.lineWrap = true
        super.add(jsp)
    }
    fun clear() {
        text.text = ""
    }
    fun appendText(str: String) {
        val document = text.document
        document.insertString(document.length, str, null)
        text.caretPosition = document.length
    }

}