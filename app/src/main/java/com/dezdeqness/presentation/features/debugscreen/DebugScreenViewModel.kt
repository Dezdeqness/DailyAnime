package com.dezdeqness.presentation.features.debugscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.settings.models.SourceTypePreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.source.SourceType
import com.dezdeqness.data.core.config.ConfigKeys
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.core.config.ConfigSettingsProvider
import com.dezdeqness.di.source.AvailableSources
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebugScreenViewModel @Inject constructor(
    private val configManager: ConfigManager,
    private val configSettingsProvider: ConfigSettingsProvider,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DebugConfigState>(DebugConfigState())
    val uiState: StateFlow<DebugConfigState> = _uiState

    fun onInitialLoading() {
        val current = ConfigKeys.entries.associateWith {
            configManager.getValue<Any>(key = ConfigKeys.getByKey(it.key))
        }
        _uiState.value = DebugConfigState(
            configValues = current,
            isOverrideEnabled = configSettingsProvider.isOverrideRemoteEnabled() == true,
            availableSources = AvailableSources.all,
        )
        viewModelScope.launch {
            val source = settingsRepository.getPreference(SourceTypePreference)
            _uiState.update { it.copy(source = source) }
        }
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

    fun saveSource(sourceType: SourceType, onSaved: () -> Unit) {
        viewModelScope.launch {
            settingsRepository.setPreference(SourceTypePreference, sourceType)
            onSaved()
        }
    }

    fun onOverrideConfigKeysClicked(value: Boolean) {
        configSettingsProvider.setOverrideRemoteEnabled(value)
        _uiState.update { it.copy(isOverrideEnabled = value, isModified = true) }
    }
}
