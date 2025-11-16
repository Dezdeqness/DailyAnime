package com.dezdeqness.feature.onboarding.selectgenres.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.HomeGenresProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectGenresViewModel @Inject constructor(
    private val configurationProvider: ConfigurationProvider,
    private val mapper: SelectGenresMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
    private val settingsRepository: SettingsRepository,
    private val homeGenresProvider: HomeGenresProvider,
) : ViewModel() {

    private val _events = Channel<SelectGenresEvent>()
    val events = _events.receiveAsFlow()

    private val selectedIds = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<SelectGenresUiState> =
        flow {
            val genres = configurationProvider.getListGenre()

            val mappedGenres = genres.map(mapper::map)

            emit(SelectGenresUiState(genres = mappedGenres))

            val selected = homeGenresProvider.getHomeSectionGenresIds()
            selectedIds.update {
                selected.toSet()
            }

            selectedIds.collect { selected ->
                emit(
                    SelectGenresUiState(
                        genres = mappedGenres,
                        selectedGenres = selected
                    )
                )
            }
        }
            .catch { throwable ->
                appLogger.logInfo(tag = TAG, "flow error", throwable)
                emit(SelectGenresUiState(genres = emptyList()))
            }
            .flowOn(coroutineDispatcherProvider.io())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SelectGenresUiState(genres = emptyList())
            )

    fun onGenreClick(id: String) {
        selectedIds.update { current ->
            val newSet = current.toMutableSet()
            if (id in newSet) {
                newSet.remove(id)
            } else if (newSet.size < 3) {
                newSet.add(id)
            }
            newSet
        }
    }

    fun onSaveClick() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            val orderedSelectedIds = uiState.value.genres
                .map { it.id }
                .filter { it in selectedIds.value }
            settingsRepository.setPreference(UserSelectedInterestsPreference, orderedSelectedIds)

            _events.send(SelectGenresEvent.Close)
        }
    }

    companion object {
        private const val TAG = "SelectGenresViewModel"
    }
}
