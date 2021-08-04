package org.bytecamp.program_repair.astor_plugin.configs

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
    name = "org.bytecamp.program_repair.astor_plugin.configs.AppSettingsState",
    storages = [Storage("AstorPluginSettings.xml")]
)
class AppSettingsState : PersistentStateComponent<AppSettingsState?> {
    var algorithm = "jGenProg"
    var backendAddress = "localhost:10000"
    override fun getState(): AppSettingsState {
        return this
    }

    override fun loadState(state: AppSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val algorithms =
            arrayOf("jGenProg", "Cardumen", "DeepRepair", "jKali", "jMutRepair", "Exastor", "Scaffold", "custom")
        val instance: AppSettingsState
            get() = ApplicationManager.getApplication().getService(AppSettingsState::class.java)
    }
}