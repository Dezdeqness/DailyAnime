package com.dezdeqness.presentation.features.animedetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class AnimeDetailsViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    private val useCase: GetAnimeDetailsUseCase,
    private val animeDetailsComposer: AnimeDetailsComposer,
) : ViewModel() {

    private val _animeDetailsStateFlow: MutableStateFlow<AnimeDetailsState> =
        MutableStateFlow(AnimeDetailsState())
    val animeDetailsStateFlow: StateFlow<AnimeDetailsState> get() = _animeDetailsStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.invoke(animeId)
                .onSuccess { details ->
                    _animeDetailsStateFlow.value = animeDetailsComposer.compose(details)
                    Log.d("AnimeDetailsViewModel", details.toString())
                }
                .onFailure {
                    Log.d("AnimeDetailsViewModel", it.toString())
                }

        }
    }

}