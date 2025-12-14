package com.github.vmaerten.filenesting.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

/**
 * Persistent settings for the File Nesting plugin.
 *
 * Settings are stored at the application level (IDE-wide, not per-project).
 */
@Service(Service.Level.APP)
@State(
    name = "FileNestingSettings",
    storages = [Storage("fileNesting.xml")]
)
class FileNestingSettings : PersistentStateComponent<FileNestingSettings.State> {

    /**
     * The settings state that gets persisted.
     */
    data class State(
        /**
         * Whether file nesting is enabled.
         */
        var enabled: Boolean = true
    )

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    companion object {
        /**
         * Get the settings instance using modern service API.
         */
        fun getInstance(): FileNestingSettings = service()
    }
}
