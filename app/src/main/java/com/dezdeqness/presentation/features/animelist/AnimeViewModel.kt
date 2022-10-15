package com.dezdeqness.presentation.features.animelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
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
    private val getAnimeListUseCase: GetAnimeListUseCase,
    private val animeUiMapper: AnimeUiMapper,
    private val animeFilterResponseConverter: AnimeFilterResponseConverter,
) : ViewModel() {

    private val _animeStateFlow: MutableStateFlow<AnimeState> = MutableStateFlow(AnimeState())

    val animeStateFlow: StateFlow<AnimeState> get() = _animeStateFlow

    private var filtersList: List<AnimeSearchFilter> = emptyList()

    private var currentPage = INITIAL_PAGE

    init {
        fetchList(page = currentPage)
    }

    fun onFabClicked() {
        val events = _animeStateFlow.value.events
        _animeStateFlow.value = _animeStateFlow.value.copy(
            events = events + Event.NavigateToFilter(filters = filtersList),
            isNeedToScrollToTop = false,
        )
    }

    fun applyFilter(filtersList: List<AnimeSearchFilter>) {
        this.filtersList = filtersList
        fetchList(page = INITIAL_PAGE)
    }

    fun onRefreshSwiped() {
        _animeStateFlow.value = _animeStateFlow.value.copy(
            isRefreshing = true,
        )
        fetchList(page = INITIAL_PAGE)
    }

    fun onLoadMore() {
        fetchList(
            page = currentPage,
            mergeWithCurrentList = true,
            isNeedToScrollToTop = false,
        )
    }

    fun onEventConsumed(event: Event) {
        val value = _animeStateFlow.value
        _animeStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    private fun fetchList(
        page: Int,
        mergeWithCurrentList: Boolean = false,
        isNeedToScrollToTop: Boolean = true,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getAnimeListUseCase.invoke(
                pageNumber = page,
                queryMap = animeFilterResponseConverter.convertSearchFilterToQueryMap(
                    filterSelectedCells()
                ),
            )
                .onSuccess { state ->
                    currentPage = state.currentPage
                    val list = state.list.map { animeUiMapper.map(it) }

                    _animeStateFlow.value = _animeStateFlow.value.copy(
                        list = if (mergeWithCurrentList) _animeStateFlow.value.list + list else list,
                        isNeedToScrollToTop = isNeedToScrollToTop,
                        isRefreshing = false,
                        hasNextPage = state.hasNextPage,
                    )
                }
                .onFailure { exception ->
                    _animeStateFlow.value = _animeStateFlow.value.copy(
                        isRefreshing = false,
                    )
                    Log.d("AnimeViewModel", exception.toString())
                }
        }
    }

    private fun filterSelectedCells() =
        filtersList.map { it.copy(items = it.items.filterNot { cellState -> cellState.state == CellState.NONE }) }
            .filter { it.items.isNotEmpty() }


    companion object {
        private const val INITIAL_PAGE = 1
    }
}
