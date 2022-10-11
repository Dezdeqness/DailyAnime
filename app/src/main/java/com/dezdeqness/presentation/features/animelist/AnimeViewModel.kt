package com.dezdeqness.presentation.features.animelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.presentation.AnimeFilterResponseConverter
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.presentation.models.CellState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val animeUiMapper: AnimeUiMapper,
    private val animeFilterResponseConverter: AnimeFilterResponseConverter,
) : ViewModel() {

    private val _animeStateFlow: MutableStateFlow<AnimeState> = MutableStateFlow(AnimeState())

    val animeStateFlow: StateFlow<AnimeState> get() = _animeStateFlow

    private var filtersList: List<AnimeSearchFilter> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getListAnimeWithQuery(
                    animeFilterResponseConverter.convertSearchFilterToQueryMap(filterSelectedCells())
                )
                .map { list -> list.map { animeUiMapper.map(it) } }
                .onSuccess { list ->
                    _animeStateFlow.value = _animeStateFlow.value.copy(
                        list = list,
                        isListChanged = true,
                    )
                }
                .onFailure { exception ->
                    Log.d("AnimeViewModel", exception.toString())
                }

        }
    }

    fun onFabClicked() {
        val events = _animeStateFlow.value.events
        _animeStateFlow.value = _animeStateFlow.value.copy(
            events = events + Event.NavigateToFilter(filters = filtersList),
            isListChanged = false,
        )
    }

    fun applyFilter(filtersList: List<AnimeSearchFilter>) {
        this.filtersList = filtersList
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getListAnimeWithQuery(
                    animeFilterResponseConverter.convertSearchFilterToQueryMap(filterSelectedCells())
                )
                .map { list -> list.map { animeUiMapper.map(it) } }
                .onSuccess { list ->
                    _animeStateFlow.value = _animeStateFlow.value.copy(
                        list = list,
                        isListChanged = true,
                    )
                }
                .onFailure { exception ->
                    Log.d("AnimeViewModel", exception.toString())
                }

        }
    }

    fun onEventConsumed(event: Event) {
        val value = _animeStateFlow.value
        _animeStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    private fun filterSelectedCells() =
        filtersList.map { it.copy(items = it.items.filterNot { cellState -> cellState.state == CellState.NONE }) }
            .filter { it.items.isNotEmpty() }

}
