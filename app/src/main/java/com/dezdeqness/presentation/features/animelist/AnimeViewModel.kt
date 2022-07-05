package com.dezdeqness.presentation.features.animelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.presentation.AnimeUiMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val animeUiMapper: AnimeUiMapper,
) : ViewModel() {

    private val _animeStateFlow: MutableStateFlow<AnimeState> = MutableStateFlow(AnimeState())
    val animeStateFlow: StateFlow<AnimeState> get() = _animeStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getListAnime()
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
