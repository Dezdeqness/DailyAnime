package com.dezdeqness.presentation.features.genericlistscreen

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.usecases.BaseListableUseCase
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
), BaseViewModel.InitialLoaded {

    private val _genericListableStateFlow = MutableStateFlow(GenericListableState())
    val genericListableStateFlow: StateFlow<GenericListableState> get() = _genericListableStateFlow

    init {
        actionConsumer.attachListener(this)
        initialPageLoad()
    }

    override val viewModelTag: String
        get() = "GenericListableViewModel"


    override fun setLoadingIndicatorVisible(isVisible: Boolean) {}

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    fun onRetryButtonClicked() {
        initialPageLoad()
    }

    private fun initialPageLoad() {
        _genericListableStateFlow.update { it.copy(status = GenericListableStatus.Loading) }
        onInitialLoad(
            action = { baseListableUseCase.invoke(id = animeId) },
            onSuccess = { state ->
                val list = state.mapNotNull(genericMapper::map)

                _genericListableStateFlow.update {
                    it.copy(
                        list = ImmutableList.copyOf(list),
                        status = GenericListableStatus.Loaded,
                    )
                }
            },
            onFailure = {
                logInfo("Error during initial loading of state of generic list", it)

                _genericListableStateFlow.update { it.copy(status = GenericListableStatus.Error) }
            }
        )
    }

}
