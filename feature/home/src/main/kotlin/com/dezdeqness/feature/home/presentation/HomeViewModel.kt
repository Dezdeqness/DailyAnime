package com.dezdeqness.feature.home.presentation

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.utils.ImageUrlUtils
import com.dezdeqness.domain.repository.HomeRepository
import com.dezdeqness.domain.usecases.GetLatestHistoryItemUseCase
import com.dezdeqness.feature.history.presentation.models.HistoryModel.HistoryUiModel
import com.dezdeqness.feature.home.presentation.models.SectionStatus
import com.dezdeqness.feature.home.presentation.models.SectionUiModel
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update

class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val homeUiMapper: HomeUiMapper,
    private val homeGenresProvider: HomeGenresProvider,
    private val sessionManager: SessionManager,
    private val configManager: ConfigManager,
    private val getLatestHistoryItemUseCase: GetLatestHistoryItemUseCase,
    private val imageUrlUtils: ImageUrlUtils,
    private val settingsRepository: SettingsRepository,
    private val homeComposer: HomeComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "HomeViewModel"

    private val _homeStateFlow: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeStateFlow: StateFlow<HomeState> get() = _homeStateFlow

    init {
        launchOnIo {
            sessionManager.sessionState.collect { _ ->
                handleProfileState()
            }
        }

        launchOnIo {
            settingsRepository
                .observePreference(UserSelectedInterestsPreference)
                .drop(1)
                .collect {
                    loadInterestsSections(resetSections = true)
                }
        }

        launchOnIo {
            val sectionsState = homeComposer.composeSectionsInitial()
            _homeStateFlow.update {
                it.copy(sectionsState = sectionsState)
            }
        }
    }

    fun onInitialLoad() {
        launchOnIo {
            handleProfileState()
        }

        loadLastHistorySection()
        loadInterestsSections()
    }

    private fun loadLastHistorySection() {
        updateLatestHistorySection { it.copy(status = SectionStatus.Loading) }

        launchOnIo {
            getLatestHistoryItemUseCase.invoke()
                .onSuccess { item ->
                    val mappedItem = item?.let {
                        HistoryUiModel(
                            name = item.itemRussian.ifEmpty { item.itemName },
                            action = HtmlCompat.fromHtml(
                                item.description,
                                FROM_HTML_MODE_COMPACT,
                            ).toString(),
                            imageUrl = imageUrlUtils.getImageWithBaseUrl(item.image),
                        )
                    }

                    updateLatestHistorySection {
                        it.copy(
                            status = SectionStatus.Loaded,
                            historyUiModel = mappedItem,
                        )
                    }
                }
                .onFailure { throwable ->
                    logInfo("Error during load of latest history item", throwable)

                    updateLatestHistorySection { it.copy(status = SectionStatus.Error) }
                }
        }
    }

    private fun loadInterestsSections(resetSections: Boolean = false) {
        launchOnIo {
            if (resetSections) {
                _homeStateFlow.update {
                    it.copy(sectionsState = homeComposer.composeSectionsInitial())
                }
            }

            if (_homeStateFlow.value.sectionsState.genreSections.isEmpty()) {
                updateSectionStatuses(SectionStatus.Loading)
            }

            homeRepository.getHomeSections(homeGenresProvider.getHomeSectionGenresIds())
                .onSuccess { homeEntity ->
                    val genreSections = homeEntity
                        .genreSections
                        .mapNotNull { item ->
                            val list = item.value.map(homeUiMapper::mapSectionAnimeModel)
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
                                status = SectionStatus.Loaded,
                            )
                        }

                    val calendarSection = homeEntity
                        .calendarSection
                        .map(homeUiMapper::mapHomeCalendarAnimeModel)

                    _homeStateFlow.update { state ->
                        state.copy(
                            sectionsState = state.sectionsState.copy(
                                genreSections = genreSections,
                                calendarSection = state.sectionsState.calendarSection.copy(
                                    items = calendarSection,
                                    status = SectionStatus.Loaded,
                                    isCalendarActionVisible = configManager.isCalendarEnabled,
                                ),
                            ),
                        )
                    }
                }
                .onFailure { throwable ->
                    updateSectionStatuses(SectionStatus.Error)

                    logInfo("Error during load home page", throwable)
                }
        }
    }

    private fun updateSectionStatuses(status: SectionStatus) {
        _homeStateFlow.update {
            it.copy(
                sectionsState = it.sectionsState.copy(
                    genreSections = it.sectionsState.genreSections.map { item ->
                        item.copy(status = status)
                    },
                    calendarSection = it.sectionsState.calendarSection.copy(status = status),
                ),
            )
        }
    }

    private fun updateLatestHistorySection(
        transform: (LatestHistoryItemSection) -> LatestHistoryItemSection,
    ) {
        _homeStateFlow.update {
            it.copy(
                sectionsState = it.sectionsState.copy(
                    latestHistoryItem = transform(it.sectionsState.latestHistoryItem),
                ),
            )
        }
    }

    private fun handleProfileState() {
        val state = sessionManager.sessionState.value
        val isAuthorized = state is SessionState.Authenticated
        val userName = (state as? SessionState.Authenticated)?.nickname.orEmpty()
        val avatarUrl = (state as? SessionState.Authenticated)?.avatar.orEmpty()

        _homeStateFlow.update {
            it.copy(
                authorizedState = it.authorizedState.copy(
                    isAuthorized = isAuthorized,
                    userName = userName,
                    avatarUrl = avatarUrl,
                ),
            )
        }
    }
}
