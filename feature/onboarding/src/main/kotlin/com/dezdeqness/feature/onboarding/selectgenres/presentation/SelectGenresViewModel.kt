package com.dezdeqness.feature.onboarding.selectgenres.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.settings.repository.UserInterestsProvider
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectGenresViewModel @Inject constructor(
    private val configurationProvider: ConfigurationProvider,
    private val mapper: SelectGenresMapper,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
    private val settingsRepository: SettingsRepository,
    private val userInterestsProvider: UserInterestsProvider,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "SelectGenresViewModel"

    private val _events = Channel<SelectGenresEvent>()
    val events = _events.receiveAsFlow()

    private val selectedIds = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<SelectGenresUiState> =
        flow {
            val genres = configurationProvider.getListGenre()

            val mappedGenres = genres.map(mapper::map)

            emit(SelectGenresUiState(genres = mappedGenres))

            val selected = userInterestsProvider.getInterestIds()
            selectedIds.update {
                selected.toSet()
            }

            selectedIds.collect { selected ->
                emit(
                    SelectGenresUiState(
                        genres = mappedGenres,
                        selectedGenres = selected,
                    ),
                )
            }
        }
            .catch { throwable ->
                logInfo("Error in select genres flow", throwable)
                emit(SelectGenresUiState(genres = emptyList()))
            }
            .flowOn(coroutineDispatcherProvider.io())
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SelectGenresUiState(genres = emptyList()),
            )

    fun onGenreClick(id: String) {
        selectedIds.update { current ->
            val newSet = current.toMutableSet()
            if (id in newSet) {
                newSet.remove(id)
            } else if (newSet.size < MAX_GENRE_COUNT) {
                newSet.add(id)
            }
            newSet
        }
    }

    fun saveSelection() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            persistSelection()
        }
    }

    fun onSaveClick() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            persistSelection()
            _events.send(SelectGenresEvent.Close)
        }
    }

    private suspend fun persistSelection() {
        val orderedSelectedIds = uiState.value.genres
            .map { it.id }
            .filter { it in selectedIds.value }
        settingsRepository.setPreference(UserSelectedInterestsPreference, orderedSelectedIds)
    }

    companion object {
        private const val MAX_GENRE_COUNT = 3
    }
}
