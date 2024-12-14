package com.dezdeqness.presentation.features.home

import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.HomeRepository
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.features.home.composable.SectionStatus
import com.dezdeqness.presentation.features.home.composable.SectionUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val actionConsumer: ActionConsumer,
    private val animeUiMapper: AnimeUiMapper,
    private val homeGenresProvider: HomeGenresProvider,
    private val accountRepository: AccountRepository,
    homeComposer: HomeComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _homeStateFlow: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState(sectionsState = homeComposer.composeSectionsInitial()))
    val homeStateFlow: StateFlow<HomeState> get() = _homeStateFlow

    init {
        actionConsumer.attachListener(this)
        launchOnIo {
            handleProfileState()
        }

        launchOnIo {
            accountRepository.authorizationState().collect { _ ->
                handleProfileState()
            }
        }
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
                homeRepository.getHomeSections(homeGenresProvider.getHomeSectionGenresIds())
            },
            onLoading = { isLoading ->
                if (isLoading) {
                    _homeStateFlow.update {
                        it.copy(
                            sectionsState = it.sectionsState.copy(
                                sections = _homeStateFlow.value.sectionsState.sections.map { item ->
                                    item.copy(status = SectionStatus.Loading)
                                }
                            )
                        )
                    }
                }
            },
            onSuccess = {
                val sections = it
                    .sections
                    .mapNotNull { item ->
                        val list = item.value.map(animeUiMapper::mapSectionAnimeModel)
                        val section = _homeStateFlow
                            .value
                            .sectionsState
                            .sections
                            .find { section -> section.numericId == item.key }
                            ?: return@mapNotNull null

                        SectionUiModel(
                            id = section.id,
                            numericId = section.numericId,
                            title = section.title,
                            items = list,
                            status = SectionStatus.Loaded
                        )
                    }

                _homeStateFlow.update { state ->
                    state.copy(
                        sectionsState = state.sectionsState.copy(
                            sections = sections,
                        ),
                    )
                }
            },
            onFailure = {
                _homeStateFlow.update {
                    it.copy(
                        sectionsState = it.sectionsState.copy(
                            sections = _homeStateFlow.value.sectionsState.sections.map { item ->
                                item.copy(status = SectionStatus.Error)
                            }
                        )
                    )
                }
            }
        )
    }

    private fun handleProfileState() {
        val isAuthorized = accountRepository.isAuthorized()
        val userName = accountRepository.getProfileLocal()?.nickname.orEmpty()
        val avatarUrl = accountRepository.getProfileLocal()?.avatar.orEmpty()

        _homeStateFlow.update {
            it.copy(
                authorizedState = it.authorizedState.copy(
                    isAuthorized = isAuthorized,
                    userName = userName,
                    avatarUrl = avatarUrl,
                )
            )
        }
    }

}
