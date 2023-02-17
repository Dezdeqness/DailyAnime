package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.presentation.event.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class AnimeDetailsViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    private val useCase: GetAnimeDetailsUseCase,
    private val animeDetailsComposer: AnimeDetailsComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded {

    private val _animeDetailsStateFlow: MutableStateFlow<AnimeDetailsState> =
        MutableStateFlow(AnimeDetailsState())
    val animeDetailsStateFlow: StateFlow<AnimeDetailsState> get() = _animeDetailsStateFlow

    init {
        launchOnIo {
            onInitialLoad(
                action = { useCase.invoke(animeId) },
                onSuccess = { details ->
                    _animeDetailsStateFlow.value = animeDetailsComposer.compose(details)
                    logInfo(message = details.toString())
                }
            )
        }
    }

    override fun viewModelTag() = "ItemDetailsViewModel"
    override fun onEventConsumed(event: Event) {
        // TODO
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO: shimmer?
    }

}
