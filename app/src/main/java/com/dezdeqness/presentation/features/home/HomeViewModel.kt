package com.dezdeqness.presentation.features.home

import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.HomeRepository
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.features.home.model.SectionStatus
import com.dezdeqness.presentation.features.home.model.SectionUiModel
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
    private val configManager: ConfigManager,
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
        launchOnIo {
            handleProfileState()
        }

        onInitialLoad(
            action = {
                homeRepository.getHomeSections(homeGenresProvider.getHomeSectionGenresIds())
            },
            onLoading = { isLoading ->
                if (isLoading && _homeStateFlow.value.sectionsState.genreSections.isEmpty()) {
                    _homeStateFlow.update {
                        it.copy(
                            sectionsState = it.sectionsState.copy(
                                genreSections = _homeStateFlow.value.sectionsState.genreSections.map { item ->
                                    item.copy(status = SectionStatus.Loading)
                                },
                                calendarSection = _homeStateFlow.value.sectionsState.calendarSection.copy(
                                    status = SectionStatus.Loading,
                                )
                            )
                        )
                    }
                }
            },
            onSuccess = {
                val genreSections = it
                    .genreSections
                    .mapNotNull { item ->
                        val list = item.value.map(animeUiMapper::mapSectionAnimeModel)
                        val section = _homeStateFlow
                            .value
                            .sectionsState
                            .genreSections
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

                val calendarSection =
                    it.calendarSection.map(animeUiMapper::mapHomeCalendarAnimeModel)

                _homeStateFlow.update { state ->
                    state.copy(
                        sectionsState = state.sectionsState.copy(
                            genreSections = genreSections,
                            calendarSection = _homeStateFlow.value.sectionsState.calendarSection.copy(
                                items = calendarSection,
                                status = SectionStatus.Loaded,
                                isCalendarActionVisible = configManager.isCalendarEnabled,
                            )
                        ),
                    )
                }
            },
            onFailure = { throwable ->
                _homeStateFlow.update {
                    it.copy(
                        sectionsState = it.sectionsState.copy(
                            genreSections = _homeStateFlow.value.sectionsState.genreSections.map { item ->
                                item.copy(status = SectionStatus.Error)
                            },
                            calendarSection = _homeStateFlow.value.sectionsState.calendarSection.copy(
                                status = SectionStatus.Error,
                            )
                        )
                    )
                }
                logInfo("Error during load home page", throwable)
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
