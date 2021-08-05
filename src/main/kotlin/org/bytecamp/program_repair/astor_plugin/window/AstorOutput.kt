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
        try {
            val document = text.document
            val lines = str.split("\n")
            for (line in lines) {
                var c: Color = Color.LIGHT_GRAY
                if (line.indexOf("INFO") != -1)
                    c = Color.GREEN
                if (line.indexOf("DEBUG") != -1)
                    c = Color.GRAY
                if (line.indexOf("WARN") != -1)
                    c = Color.ORANGE
                if (line.indexOf("ERROR") != -1)
                    c = Color.RED

                val sc = StyleContext.getDefaultStyleContext()
                val style = sc.addStyle("font folor", null)
                StyleConstants.setForeground(style, c)
                document.insertString(document.length, line, style)
                document.insertString(document.length, "\n", style)
            }
            if (document.length > 20000) {
                document.remove(0, 10000)
            }
            text.caretPosition = document.length

        } catch (thr: Throwable) {
            thr.printStackTrace()
        }

    }
}