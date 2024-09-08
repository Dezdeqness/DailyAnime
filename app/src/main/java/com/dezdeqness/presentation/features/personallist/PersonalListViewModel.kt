package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.model.FullAnimeStatusesEntity
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.PersonalListFilterRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.OpenMenuPopupFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.features.userrate.EditRateUiModel
import com.dezdeqness.presentation.message.MessageConsumer
import com.dezdeqness.presentation.models.UserRateUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PersonalListViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val personalListFilterRepository: PersonalListFilterRepository,
    private val accountRepository: AccountRepository,
    private val actionConsumer: ActionConsumer,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.Refreshable, BaseViewModel.InitialLoaded {

    private var currentRibbonId: String? = null

    private var query: String? = null

    private var ribbonRaw: FullAnimeStatusesEntity = FullAnimeStatusesEntity(listOf())

    private var userRatesList: List<UserRateEntity> = listOf()

    private var workerJob: Job? = null

    private val _personalListStateFlow: MutableStateFlow<PersonalListState> =
        MutableStateFlow(PersonalListState())
    val personalListStateFlow: StateFlow<PersonalListState> get() = _personalListStateFlow

    init {
        initialLoad()
        actionConsumer.attachListener(this)
    }

    override val viewModelTag = "PersonalListViewModel"

    override fun onPullDownRefreshed() {
        if (currentRibbonId != null) {
            currentRibbonId?.let { status ->
                cancelJobIfActive()

                workerJob = onPullDownRefreshed(
                    collector = userRatesRepository.getUserRates(
                        status = status,
                        page = INITIAL_PAGE,
                        onlyRemote = true,
                    ),
                    onSuccess = { list ->
                        userRatesList = list

                        val uiItems = personalListComposer.compose(
                            filter = personalListFilterRepository.getFilter(),
                            entityList = list,
                            query = query,
                        )

                        _personalListStateFlow.value = _personalListStateFlow.value.copy(
                            items = uiItems,
                            isEmptyStateShowing = userRatesList.isEmpty(),
                        )
                    },
                    onFailure = {
                        logInfo(
                            "Error during pull down of personal list",
                            it,
                        )
                    }
                )
            }
        } else {
            initialLoad()
        }
    }

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    fun onScrollNeed() {
        if (personalListStateFlow.value.isScrollNeed) {
            _personalListStateFlow.update {
                _personalListStateFlow.value.copy(isScrollNeed = false)
            }
            onEventReceive(ScrollToTop)
        }
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    fun onRibbonItemSelected(id: String) {
        if (_personalListStateFlow.value.ribbon.find { it.id == id }?.isSelected == true) {
            return
        }
        currentRibbonId = id
        var ribbon = personalListComposer.composeStatuses(ribbonRaw)

        ribbon = personalListComposer.applySelected(
            items = ribbon,
            currentId = currentRibbonId
        )

        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            ribbon = ribbon,
            items = listOf(),
        )
        launchOnIo {
            currentRibbonId?.let { status ->
                fetchCategory(status = status)
            }
        }
    }

    fun onSortChanged(sort: String?) {
        if (sort == null) return

        launchOnIo {
            personalListFilterRepository.setSort(sort)

            val uiItems = personalListComposer.compose(
                filter = personalListFilterRepository.getFilter(),
                entityList = userRatesList,
                query = query,
            )

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = uiItems,
                isScrollNeed = true,
            )
        }
    }

    fun onFilterButtonClicked() {
        launchOnIo {
            val sort = personalListFilterRepository.getFilter().sort.sort
            onEventReceive(OpenMenuPopupFilter(sort))
        }
    }

    fun onQueryChanged(query: String) {
        if (this.query == query) {
            return
        }

        this.query = query

        launchOnIo {
            val uiItems = personalListComposer.compose(
                filter = personalListFilterRepository.getFilter(),
                entityList = userRatesList,
                query = query,
            )

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = uiItems,
                isEmptyStateShowing = uiItems.isEmpty(),
                isScrollNeed = true,
            )
        }
    }

    fun onEditRateClicked(editRateId: Long) {
        personalListStateFlow
            .value
            .items
            .filterIsInstance<UserRateUiModel>()
            .first { it.rateId == editRateId }
            .let {
                onEventReceive(
                    NavigateToEditRate(
                        rateId = editRateId,
                        title = it.name,
                        overallEpisodes = it.overallEpisodes,
                    )
                )
            }

    }

    fun onUserRateChanged(userRate: EditRateUiModel?) {
        if (userRate == null) {
            return
        }

        onInitialLoad(
            action = {
                userRatesRepository.updateUserRate(
                    rateId = userRate.rateId,
                    status = userRate.status,
                    episodes = userRate.episodes,
                    score = userRate.score,
                    comment = userRate.comment,
                )
            },
            onSuccess = {
                onEditSuccessMessage()
                initialLoad()
            },
            onFailure = {
                logInfo("Error during user rate changes of personal list", it)

                onEditErrorMessage()
            }
        )
    }

    private fun onEditErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
        }
    }

    private fun onEditSuccessMessage() {
        launchOnIo {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditCreateSuccessMessage())
        }
    }

    // TODO: Add fetching when app from foreground
    private fun initialLoad() {
        onInitialLoad(
            collector = accountRepository.getProfileDetails(),
            onSuccess = { details ->
                ribbonRaw = details.fullAnimeStatusesEntity

                var ribbon = personalListComposer.composeStatuses(ribbonRaw)

                if (ribbon.isEmpty()) {
                    _personalListStateFlow.value = _personalListStateFlow.value.copy(
                        isEmptyStateShowing = true,
                        isPullDownRefreshing = false,
                    )
                } else {
                    if (currentRibbonId == null) {
                        currentRibbonId = ribbon.first().id
                    }

                    if (ribbon.none { it.id == currentRibbonId }) {
                        currentRibbonId = ribbon.first().id
                    }

                    ribbon = personalListComposer.applySelected(
                        items = ribbon,
                        currentId = currentRibbonId,
                    )

                    _personalListStateFlow.value = _personalListStateFlow.value.copy(
                        ribbon = ribbon,
                        isEmptyStateShowing = false,
                    )
                    currentRibbonId?.let { status ->
                        fetchCategory(status = status)
                    }
                }
            },
            onFailure = {
                logInfo("Error during initial loading of state of personal list", it)
            }
        )
    }

    private fun cancelJobIfActive() {
        if (workerJob != null && workerJob?.isActive == true) {
            workerJob?.cancel()
        }
    }

    private fun fetchCategory(status: String) {
        cancelJobIfActive()

        workerJob = onInitialLoad(
            collector = userRatesRepository.getUserRates(status = status, page = INITIAL_PAGE),
            onSuccess = { list ->
                userRatesList = list

                val uiItems = personalListComposer.compose(
                    filter = personalListFilterRepository.getFilter(),
                    entityList = userRatesList,
                    query = query,
                )

                _personalListStateFlow.value = _personalListStateFlow.value.copy(
                    items = uiItems,
                    isEmptyStateShowing = userRatesList.isEmpty(),
                )
            },
            onFailure = {
                logInfo("Error during fetch of category with status: $status of personal list", it)
            }
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
