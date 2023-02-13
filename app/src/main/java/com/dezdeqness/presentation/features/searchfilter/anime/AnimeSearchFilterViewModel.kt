package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.presentation.Event
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.presentation.models.CellState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class AnimeSearchFilterViewModel @Inject constructor(
    private val animeSearchFilterComposer: AnimeSearchFilterComposer,
    @Named("searchFiltersList") private val list: List<AnimeSearchFilter>,
    searchFilterRepository: SearchFilterRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _animeSearchFilterStateFlow: MutableStateFlow<AnimeSearchFilterState> =
        MutableStateFlow(AnimeSearchFilterState())
    val animeSearchFilterStateFlow: StateFlow<AnimeSearchFilterState> get() = _animeSearchFilterStateFlow

    private val selectedCells: MutableList<AnimeCell> = mutableListOf()

    init {
        if (list.isEmpty()) {
            searchFilterRepository.getFilterConfiguration().run {
                _animeSearchFilterStateFlow.value =
                    AnimeSearchFilterState(items = animeSearchFilterComposer.compose(this))
            }
        } else {
            _animeSearchFilterStateFlow.value =
                AnimeSearchFilterState(items = list)
        }
    }

    override fun viewModelTag() = "AnimeSearchFilterViewModel"

    override fun onEventConsumed(event: Event) {
        val value = _animeSearchFilterStateFlow.value
        _animeSearchFilterStateFlow.value = value.copy(
            listEvents = value.listEvents.toMutableList() - event
        )
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
        val animeSearchFilters = _animeSearchFilterStateFlow.value.items

        applyFilter(animeSearchFilters)
    }

    fun onResetButtonClicked() {
        applyFilter(listOf())
    }

    private fun applyFilter(animeSearchFilters: List<AnimeSearchFilter>) {
        _animeSearchFilterStateFlow.value =
            _animeSearchFilterStateFlow.value.copy(
                listEvents = _animeSearchFilterStateFlow.value.listEvents + Event.ApplyFilter(
                    filters = animeSearchFilters
                )
            )
    }

}
