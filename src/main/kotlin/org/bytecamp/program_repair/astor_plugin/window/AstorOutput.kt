package org.bytecamp.program_repair.astor_plugin.window

import org.jetbrains.debugger.sourcemap.doCanonicalize
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.text.Document
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

import java.awt.Color

class AstorOutput : JPanel() {
    var text = JTextPane()
    var jsp = com.intellij.ui.components.JBScrollPane(text)
    val sc = StyleContext.getDefaultStyleContext()
    val style = sc.addStyle("font folor", null)

    init {
        layout = BorderLayout()
        text.isEditable = false
        text.autoscrolls = true
//        text.lineWrap = true
        super.add(jsp)
    }

    fun clear() {
        text.text = ""
    }

    fun appendText(str: String) {
        println(str)
        val document = text.document
        var c: Color
        when (str) {
            "DEBUG" -> c = Color.BLUE
            "INFO" -> c = Color.GREEN
            "ERROR" -> c = Color.RED
            else -> c = Color.WHITE
        }
        StyleConstants.setForeground(style, c)
        document.insertString(document.length, str, style)
        text.caretPosition = document.length
    }
}