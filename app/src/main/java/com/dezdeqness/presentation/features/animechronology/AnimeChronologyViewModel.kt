package com.dezdeqness.presentation.features.animechronology

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

class AnimeChronologyViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    private val animeRepository: AnimeRepository,
    private val animeChronologyUiMapper: AnimeChronologyUiMapper,
    private val actionConsumer: ActionConsumer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable {

    private val _chronologyStateFlow = MutableStateFlow(AnimeChronologyState())
    val chronologyStateFlow: StateFlow<AnimeChronologyState> get() = _chronologyStateFlow

    init {
        actionConsumer.attachListener(this)
        initialPageLoad()
    }

    override val viewModelTag = "AnimeSimilarViewModel"

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _chronologyStateFlow.value = _chronologyStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _chronologyStateFlow.value = _chronologyStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = {
                animeRepository.getChronology(id = animeId)
            },
            onSuccess = { state ->
                val list = state.map(animeChronologyUiMapper::map)

                _chronologyStateFlow.value = _chronologyStateFlow.value.copy(
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
                animeRepository.getChronology(id = animeId)
            },
            onSuccess = { state ->
                val list = state.map(animeChronologyUiMapper::map)

                _chronologyStateFlow.value = _chronologyStateFlow.value.copy(
                    list = list,
                    isEmptyStateShowing = list.isEmpty(),
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                _chronologyStateFlow.value = _chronologyStateFlow.value.copy(
                    isErrorStateShowing = true,
                )
            }
        )
    }
}
