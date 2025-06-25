package com.dezdeqness.presentation.features.debugscreen

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.core.config.ConfigKeys
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.core.config.ConfigSettingsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DebugScreenViewModel @Inject constructor(
    private val configManager: ConfigManager,
    private val configSettingsProvider: ConfigSettingsProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DebugConfigState>(DebugConfigState())
    val uiState: StateFlow<DebugConfigState> = _uiState

    fun onInitialLoading() {
        val current = ConfigKeys.entries.associateWith {
            configManager.getValue<Any>(key = ConfigKeys.getByKey(it.key))
        }
        _uiState.value = DebugConfigState(
            configValues = current,
            isOverrideEnabled = configSettingsProvider.isOverrideRemoteEnabled() == true
        )
    }

    fun updateConfigValue(key: ConfigKeys, value: Any) {
        configManager.setConfigKey(key, value)
        _uiState.update {
            it.copy(
                configValues = it.configValues + (key to value),
                isModified = true,
            )
        }
    }

    fun onOverrideConfigKeysClicked(value: Boolean) {
        configSettingsProvider.setOverrideRemoteEnabled(value)
        _uiState.update { it.copy(isOverrideEnabled = value, isModified = true) }
    }
}
