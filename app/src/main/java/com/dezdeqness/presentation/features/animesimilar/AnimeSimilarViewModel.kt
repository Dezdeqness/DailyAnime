package com.dezdeqness.presentation.features.animesimilar

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AnimeRepository
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class AnimeSimilarViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    private val animeRepository: AnimeRepository,
    private val animeSimilarUiMapper: AnimeSimilarUiMapper,
    private val actionConsumer: ActionConsumer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable {

    private val _similarStateFlow = MutableStateFlow(AnimeSimilarState())
    val similarStateFlow: StateFlow<AnimeSimilarState> get() = _similarStateFlow

    init {
        actionConsumer.attachListener(this)
        initialPageLoad()
    }

    override val viewModelTag = "AnimeSimilarViewModel"

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _similarStateFlow.value = _similarStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _similarStateFlow.value = _similarStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = {
                animeRepository.getSimilar(id = animeId)
            },
            onSuccess = { state ->
                val list = state.map(animeSimilarUiMapper::map)

                _similarStateFlow.value = _similarStateFlow.value.copy(
                    list = list,
                    isErrorStateShowing = false,
                )
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    private fun initialPageLoad() {
        onInitialLoad(
            action = {
                animeRepository.getSimilar(id = animeId)
            },
            onSuccess = { state ->
                val list = state.map(animeSimilarUiMapper::map)

                _similarStateFlow.value = _similarStateFlow.value.copy(
                    list = list,
                    isEmptyStateShowing = list.isEmpty(),
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                _similarStateFlow.value = _similarStateFlow.value.copy(
                    isErrorStateShowing = true,
                )
            }
        )
    }
}
