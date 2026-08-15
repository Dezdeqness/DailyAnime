package com.dezdeqness.feature.searchfilter.presentation

import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.contract.filter.repository.SearchFilterRepository
import com.dezdeqness.feature.searchfilter.presentation.models.SelectedCell
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class AnimeSearchFilterViewModel @Inject constructor(
    private val animeSearchFilterComposer: AnimeSearchFilterComposer,
    private val searchFilterRepository: SearchFilterRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "AnimeSearchFilterViewModel"

    private val _animeSearchFilterStateFlow: MutableStateFlow<AnimeSearchFilterState> =
        MutableStateFlow(AnimeSearchFilterState())
    val animeSearchFilterStateFlow: StateFlow<AnimeSearchFilterState> get() = _animeSearchFilterStateFlow

    private val appliedFilterChannel = Channel<List<SearchSectionUiModel>>(Channel.BUFFERED)
    val appliedFilters: Flow<List<SearchSectionUiModel>> = appliedFilterChannel.receiveAsFlow()

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
        resetFilter()
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
        appliedFilterChannel.trySend(animeSearchFilters)
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
                        ),
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
                        selectedCells = it.selectedCells.filter { it.id != cellId }.toSet(),
                    )
                }

                it.copy(
                    selectedCells = it.selectedCells - cellId,
                )
            }
    }

    private fun resetFilter() {
        _animeSearchFilterStateFlow.update {
            it.copy(
                items = listOf(),
                selectedCells = setOf(),
                isFilterVisible = false,
                generalSelectedCells = false,
            )
        }
        appliedFilterChannel.trySend(listOf())
    }
}
