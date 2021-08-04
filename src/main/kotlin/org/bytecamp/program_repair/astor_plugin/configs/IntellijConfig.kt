package org.bytecamp.program_repair.astor_plugin.configs

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class UiComponent {
    var backendAddress = JBTextField()
    val algorithm = JBList(AppSettingsState.algorithms.toList())
    val myMainPanel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("BackendAddress: "), backendAddress, 1, false)
        .addLabeledComponent(JBLabel("Algorithm: "), JBScrollPane(algorithm), 1, false)
        .addComponentFillVertically(JPanel(), 0)
        .panel
    init {
        algorithm.selectedIndex = 0
    }
}

class IntellijConfig : Configurable {
    private var component: UiComponent? = null
    override fun createComponent(): JComponent {
        component = UiComponent()
        return component!!.myMainPanel
    }

    override fun isModified(): Boolean {
        val settings: AppSettingsState = AppSettingsState.instance
        var modified: Boolean = component!!.backendAddress.text != settings.backendAddress
        modified = modified || (component!!.algorithm.selectedValue != settings.algorithm)
        return modified
    }

    override fun apply() {
        val settings: AppSettingsState = AppSettingsState.instance
        settings.backendAddress = component!!.backendAddress.text
        settings.algorithm = component!!.algorithm.selectedValue
    }

    override fun getDisplayName(): String {
        return "Astor Settings"
    }

    override fun reset() {
        val settings: AppSettingsState = AppSettingsState.instance
        component!!.algorithm.setSelectedValue(settings.algorithm, true)
        component!!.backendAddress.text = settings.backendAddress
    }

    override fun disposeUIResources() {
        component = null
    }
}