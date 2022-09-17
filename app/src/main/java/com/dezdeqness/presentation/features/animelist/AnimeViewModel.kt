package com.dezdeqness.presentation.features.animelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.presentation.AnimeFilterResponseConverter
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.models.AnimeSearchFilter
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

    private var filtersList: Collection<AnimeSearchFilter> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getListAnimeWithQuery(
                    animeFilterResponseConverter.convertSearchFilterToQueryMap(
                        filtersList
                    )
                )
                .map { list -> list.map { animeUiMapper.map(it) } }
                .onSuccess { list ->
                    _animeStateFlow.value = AnimeState(list = list)
                }
                .onFailure { exception ->
                    Log.d("AnimeViewModel", exception.toString())
                }

        }
    }

    fun applyFilter(filtersList: Collection<AnimeSearchFilter>) {
        this.filtersList = filtersList
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getListAnimeWithQuery(
                    animeFilterResponseConverter.convertSearchFilterToQueryMap(
                        filtersList
                    )
                )
                .map { list -> list.map { animeUiMapper.map(it) } }
                .onSuccess { list ->
                    _animeStateFlow.value = AnimeState(list = list)
                }
                .onFailure { exception ->
                    Log.d("AnimeViewModel", exception.toString())
                }

        }
    }

}
