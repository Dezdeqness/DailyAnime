package com.dezdeqness.presentation.features.home

import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.repository.HomeRepository
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val actionConsumer: ActionConsumer,
    private val animeUiMapper: AnimeUiMapper,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _homeStateFlow: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState())
    val homeStateFlow: StateFlow<HomeState> get() = _homeStateFlow

    init {
        actionConsumer.attachListener(this)
    }

    override val viewModelTag = "HomeViewModel"

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    fun onInitialLoad() {
        onInitialLoad(
            action = {
                homeRepository.getHomeSections()
            },
            onLoading = { isLoading ->
                _homeStateFlow.update {
                    it.copy(
                        sectionsState = it.sectionsState.copy(
                            status = if (isLoading) SectionStatus.Loading else SectionStatus.Loaded
                        ),
                    )
                }
            },
            onSuccess = {
                val sections = it
                    .sections
                    .map {
                        item ->
                        val list = item.value.map(animeUiMapper::mapSectionAnimeModel)
                        SectionUiModel(
                            id = item.key.id,
                            title = item.key.name,
                            items = list,
                        )
                    }

                _homeStateFlow.update { state ->
                    state.copy(
                        sectionsState = state.sectionsState.copy(
                            status = SectionStatus.Loaded,
                            sections = sections,
                        ),

                    )
                }
            },
            onFailure = {
                _homeStateFlow.update {
                    it.copy(
                        sectionsState = it.sectionsState.copy(
                            status = SectionStatus.Error
                        ),
                    )
                }
            }
        )
    }

}
