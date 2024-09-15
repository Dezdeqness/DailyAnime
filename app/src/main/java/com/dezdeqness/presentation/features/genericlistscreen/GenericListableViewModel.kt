package com.dezdeqness.presentation.features.genericlistscreen

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.usecases.BaseListableUseCase
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class GenericListableViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    private val genericMapper: GenericListableUiMapper,
    private val baseListableUseCase: BaseListableUseCase,
    private val actionConsumer: ActionConsumer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
coroutineDispatcherProvider = coroutineDispatcherProvider,
appLogger = appLogger,
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable {

    private val _genericListableStateFlow = MutableStateFlow(GenericListableState())
    val genericListableStateFlow: StateFlow<GenericListableState> get() = _genericListableStateFlow

    init {
        actionConsumer.attachListener(this)
        initialPageLoad()
    }

    override val viewModelTag: String
        get() = "GenericListableViewModel"


    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _genericListableStateFlow.value = _genericListableStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _genericListableStateFlow.value = _genericListableStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }


    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = {
                baseListableUseCase.invoke(id = animeId)
            },
            onSuccess = { state ->
                val list = state.mapNotNull(genericMapper::map)

                _genericListableStateFlow.value = _genericListableStateFlow.value.copy(
                    list = list,
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                logInfo("Error during pull down of generic list", it)
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
                baseListableUseCase.invoke(id = animeId)
            },
            onSuccess = { state ->
                val list = state.mapNotNull(genericMapper::map)

                _genericListableStateFlow.value = _genericListableStateFlow.value.copy(
                    list = list,
                    isEmptyStateShowing = list.isEmpty(),
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                logInfo("Error during initial loading of state of generic list", it)

                _genericListableStateFlow.value = _genericListableStateFlow.value.copy(
                    isErrorStateShowing = true,
                )
            }
        )
    }

}
