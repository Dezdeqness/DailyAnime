package com.dezdeqness.presentation.features.searchfilter

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.presentation.event.ApplyFilter
import com.dezdeqness.presentation.models.SearchSectionUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AnimeSearchFilterViewModel @Inject constructor(
    private val animeSearchFilterComposer: AnimeSearchFilterComposer,
    private val searchFilterRepository: SearchFilterRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _animeSearchFilterStateFlow: MutableStateFlow<AnimeSearchFilterState> =
        MutableStateFlow(AnimeSearchFilterState())
    val animeSearchFilterStateFlow: StateFlow<AnimeSearchFilterState> get() = _animeSearchFilterStateFlow

    fun onFiltersReceived(models: List<SearchSectionUiModel>) {
        _animeSearchFilterStateFlow.update {
            it.copy(isFilterVisible = true)
        }
        val mappedModels = if (models.isEmpty()) {
            val list = searchFilterRepository.getFilterConfiguration()

            animeSearchFilterComposer.compose(list).map { MutableStateFlow(it) }
        } else {
            models.map { MutableStateFlow(it) }
        }

        _animeSearchFilterStateFlow.update {
            it.copy(items = mappedModels)
        }
    }

    override val viewModelTag = "AnimeSearchFilterViewModel"

    fun onCellClicked(innerId: String, cellId: String, isSelected: Boolean) {
        if (isSelected) {
            onCellRemoved(innerId = innerId, cellId = cellId)
        } else {
            onCellAdded(innerId = innerId, cellId = cellId)
        }
    }

    fun onApplyButtonClicked() {
        val animeSearchFilters = _animeSearchFilterStateFlow.value.items.map { it.value }

        applyFilter(animeSearchFilters)
    }

    fun onResetButtonClicked() {
        applyFilter(listOf())
    }

    fun onDismissed() {
        _animeSearchFilterStateFlow.update {
            it.copy(isFilterVisible = false)
        }
    }

    private fun applyFilter(animeSearchFilters: List<SearchSectionUiModel>) {
        _animeSearchFilterStateFlow.update {
            it.copy(
                items = listOf(),
                isFilterVisible = false,
            )
        }
        onEventReceive(ApplyFilter(filters = animeSearchFilters))
    }

    private fun onCellAdded(innerId: String, cellId: String) {
        val list = _animeSearchFilterStateFlow.value
        list
            .items
            .first { it.value.innerId == innerId }
            .update {
                val cell = it.items.find { it.id == cellId }

                _animeSearchFilterStateFlow.update {
                    it.copy(
                        selectedCells = it.selectedCells + SelectedCell(
                            sectionId = innerId,
                            id = cellId,
                            displayName = cell?.displayName.orEmpty(),
                        )
                    )
                }

                it.copy(
                    selectedCells = it.selectedCells + cellId,
                )
            }
    }

    private fun onCellRemoved(innerId: String, cellId: String) {
        val list = _animeSearchFilterStateFlow.value
        list
            .items
            .first { it.value.innerId == innerId }
            .update {
                _animeSearchFilterStateFlow.update {
                    it.copy(
                        selectedCells = it.selectedCells.filter { it.id != cellId }.toSet()
                    )
                }

                it.copy(
                    selectedCells = it.selectedCells - cellId,
                )
            }
    }

}
