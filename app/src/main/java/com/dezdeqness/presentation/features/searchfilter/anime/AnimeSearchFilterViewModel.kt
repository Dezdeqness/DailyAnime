package com.dezdeqness.presentation.features.searchfilter.anime

import androidx.lifecycle.ViewModel
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.CellState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AnimeSearchFilterViewModel @Inject constructor(
    private val animeSearchFilterComposer: AnimeSearchFilterComposer,
    searchFilterRepository: SearchFilterRepository,
) : ViewModel() {

    private val _animeSearchFilterStateFlow: MutableStateFlow<AnimeSearchFilterState> =
        MutableStateFlow(AnimeSearchFilterState())
    val animeSearchFilterStateFlow: StateFlow<AnimeSearchFilterState> get() = _animeSearchFilterStateFlow

    private val selectedCells: MutableList<AnimeCell> = mutableListOf()

    init {
        searchFilterRepository.getFilterConfiguration().also {
            _animeSearchFilterStateFlow.value =
                AnimeSearchFilterState(items = animeSearchFilterComposer.compose(it))
        }
    }

    fun onCellClicked(item: AnimeCell) {
        when (item.state) {
            CellState.NONE -> {
                item.state = CellState.INCLUDE
                selectedCells.add(item)
            }
            CellState.EXCLUDE -> {
                item.state = CellState.INCLUDE
            }
            else -> {
                item.state = CellState.NONE
                selectedCells.remove(item)
            }
        }
        _animeSearchFilterStateFlow.value =
            _animeSearchFilterStateFlow.value.copy(currentCellUpdate = item)

    }

    fun onCellLongClicked(item: AnimeCell) {
        if (item.state == CellState.NONE) {
            item.state = CellState.EXCLUDE
            selectedCells.add(item)
        } else if (item.state == CellState.INCLUDE) {
            item.state = CellState.EXCLUDE
        }
        _animeSearchFilterStateFlow.value =
            _animeSearchFilterStateFlow.value.copy(currentCellUpdate = item)
    }

    fun nullifyCurrentCell() {
        if (_animeSearchFilterStateFlow.value.currentCellUpdate == null) {
            return
        }
        _animeSearchFilterStateFlow.value =
            _animeSearchFilterStateFlow.value.copy(currentCellUpdate = null)
    }

    fun onApplyButtonClicked() {
        val filters = _animeSearchFilterStateFlow.value

        val animeSearchFilters = filters.items
            .map { it.copy(items = it.items.filterNot { cellState -> cellState.state == CellState.NONE }) }
            .filter { it.items.isNotEmpty() }

        _animeSearchFilterStateFlow.value =
            _animeSearchFilterStateFlow.value.copy(
                listEvents = _animeSearchFilterStateFlow.value.listEvents + Event.ApplyFilter(
                    filters = animeSearchFilters
                )
            )
    }

    fun onEventConsumed(event: Event) {
        val value = _animeSearchFilterStateFlow.value
        _animeSearchFilterStateFlow.value = value.copy(
            listEvents = value.listEvents.toMutableList() - event
        )
    }

}
