package com.dezdeqness.presentation.features.searchfilter.anime

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AnimeSearchFilterViewModel @Inject constructor(
    private val animeSearchFilterComposer: AnimeSearchFilterComposer,
) : ViewModel() {

    private val _animeSearchFilterStateFlow: MutableStateFlow<AnimeSearchFilterState> =
        MutableStateFlow(AnimeSearchFilterState(items = animeSearchFilterComposer.compose()))
    val animeSearchFilterStateFlow: StateFlow<AnimeSearchFilterState> get() = _animeSearchFilterStateFlow

    init {
    }

}
