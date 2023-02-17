package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
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
import com.dezdeqness.presentation.event.NavigateToSortFilter
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PersonalListViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val personalListFilterRepository: PersonalListFilterRepository,
    private val accountRepository: AccountRepository,
    private val actionConsumer: ActionConsumer,
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

    private val _personalListStateFlow: MutableStateFlow<PersonalListState> =
        MutableStateFlow(PersonalListState())
    val personalListStateFlow: StateFlow<PersonalListState> get() = _personalListStateFlow

    init {
        actionConsumer.attachListener(this)
    }

    override fun viewModelTag() = "PersonalListViewModel"
    override fun onPullDownRefreshed() {
        currentRibbonId?.let { status ->
            onPullDownRefreshed(
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
                    )
                },
            )
        }
    }

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO
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

    // TODO: Add fetching when app from foreground
    fun loadPersonalList() {
        launchOnIo {
            accountRepository.getProfileDetails().collect { result ->
                result.onSuccess { details ->

                    ribbonRaw = details.fullAnimeStatusesEntity

                    val ribbon = personalListComposer.composeStatuses(ribbonRaw, currentRibbonId)

                    if (currentRibbonId == null) {
                        if (ribbon.isEmpty()) {
                            return@onSuccess
                        }
                        currentRibbonId = ribbon.first().id
                    }

                    if (ribbon.isEmpty()) {
                        // TODO: Empty state
                    } else {
                        _personalListStateFlow.value = _personalListStateFlow.value.copy(
                            ribbon = ribbon,
                        )
                        currentRibbonId?.let { status ->
                            initialLoad(status = status)
                        }
                    }
                }
            }
        }
    }

    fun onRibbonItemSelected(id: String) {
        if (_personalListStateFlow.value.ribbon.find { it.id == id }?.isSelected == true) {
            return
        }
        currentRibbonId = id
        val ribbon = personalListComposer.composeStatuses(ribbonRaw, id)
        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            ribbon = ribbon,
            items = listOf(),
        )
        launchOnIo {
            currentRibbonId?.let { status ->
                initialLoad(status = status)
            }
        }
    }

    fun onOrderChanged(isAscending: Boolean) {
        launchOnIo {
            val order = !isAscending
            personalListFilterRepository.setOrder(order)

            val uiItems = personalListComposer.compose(
                filter = personalListFilterRepository.getFilter(),
                entityList = userRatesList,
                query = query,
            )

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = uiItems,
            )
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
            )
        }
    }

    fun onSortClicked() {
        launchOnIo {
            val filter = personalListFilterRepository.getFilter()
            val events = _personalListStateFlow.value.events

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                events = events + NavigateToSortFilter(
                    currentSort = filter.sort.sort,
                ),
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
                currentRibbonId?.let { status ->
                    initialLoad(status = status)
                }

            }
        )
    }

    private fun initialLoad(status: String) {
        onInitialLoad(
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
                )
            },
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
