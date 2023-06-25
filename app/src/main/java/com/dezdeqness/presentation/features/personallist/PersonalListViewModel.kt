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
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventListener
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.OpenMenuPopupFilter
import com.dezdeqness.presentation.event.ScrollToTop
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
), BaseViewModel.Refreshable, BaseViewModel.InitialLoaded, EventListener {

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

    override fun viewModelTag() = "PersonalListViewModel"

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

    override fun onEventConsumed(event: Event) {
        val value = _personalListStateFlow.value
        _personalListStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    override fun onEventReceive(event: Event) {
        val events = _personalListStateFlow.value.events
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            events = events + event,
        )
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
                events = _personalListStateFlow.value.events + ScrollToTop,
            )
        }
    }

    fun onFilterButtonClicked() {
        launchOnIo {
            val events = _personalListStateFlow.value.events

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                events = events + OpenMenuPopupFilter,
            )
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
                events = _personalListStateFlow.value.events + ScrollToTop,
            )
        }
    }

    fun onEditRateClicked(editRateId: Long) {
        val events = _personalListStateFlow.value.events

        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            events = events + NavigateToEditRate(
                rateId = editRateId,
            ),
        )
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
                )
            },
            onSuccess = {
                onEditSuccessMessage()
                initialLoad()
            },
            onFailure = {
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
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
