package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.settings.models.StatusesOrderPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.contract.user.model.FullAnimeStatusesEntity
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.domain.repository.PersonalListFilterRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.features.userrate.EditRateUiModel
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class)
class PersonalListViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val personalListFilterRepository: PersonalListFilterRepository,
    private val userRepository: UserRepository,
    private val actionConsumer: ActionConsumer,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    private val settingsRepository: SettingsRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.Refreshable, BaseViewModel.InitialLoaded {

    private var currentRibbonId: String? = null

    private val _queryFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val queryFlow: StateFlow<String?> = _queryFlow

    private var ribbonRaw: FullAnimeStatusesEntity = FullAnimeStatusesEntity(listOf())

    private var userRatesList: List<UserRateEntity> = listOf()

    private var workerJob: Job? = null

    private val _personalListStateFlow: MutableStateFlow<PersonalListState> =
        MutableStateFlow(PersonalListState())
    val personalListStateFlow: StateFlow<PersonalListState> get() = _personalListStateFlow

    init {
        actionConsumer.attachListener(this)

        launchOnIo {
            settingsRepository
                .observePreference(StatusesOrderPreference)
                .drop(1)
                .collect {
                    val ribbon = personalListComposer.composeStatuses(ribbonRaw)
                    _personalListStateFlow.update {
                        it.copy(ribbon = ribbon)
                    }
                }
        }

        launchOnIo {
            queryFlow
                .debounce(300)
                .distinctUntilChanged()
                .mapNotNull { it }
                .collectLatest { query ->
                    val uiItems = personalListComposer.compose(
                        filter = personalListFilterRepository.getFilter(),
                        entityList = userRatesList,
                        query = query,
                    )

                    _personalListStateFlow.value = _personalListStateFlow.value.copy(
                        items = uiItems,
                        placeholder = if (uiItems.isEmpty()) Placeholder.UserRate.Empty else Placeholder.None,
                        isScrollNeed = true,
                    )
                }
        }
    }

    override val viewModelTag = "PersonalListViewModel"

    override fun onPullDownRefreshed() {
        val state = _personalListStateFlow.value

        if (state.placeholder is Placeholder.UserRate || currentRibbonId != null) {
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
                            query = _queryFlow.value,
                        )

                        _personalListStateFlow.value = _personalListStateFlow.value.copy(
                            items = uiItems,
                            placeholder = if (userRatesList.isEmpty()) {
                                Placeholder.Ribbon.Empty
                            } else {
                                Placeholder.None
                            },
                        )
                    },
                    onFailure = {
                        logInfo(
                            "Error during pull down of personal list",
                            it,
                        )
                        _personalListStateFlow.update { state ->
                            state.copy(placeholder = Placeholder.Ribbon.Error)
                        }
                    }
                )
            }
        } else {
            onInitialLoad()
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

    // TODO: Add fetching when app from foreground
    fun onInitialLoad() {
        launchOnIo {
            val sortId = personalListFilterRepository.getFilter().sort.sort
            _personalListStateFlow.update {
                it.copy(currentSortId = sortId)
            }
        }

        onInitialLoad(
            collector = userRepository.getProfileDetails(),
            onSuccess = { details ->
                ribbonRaw = details.fullAnimeStatusesEntity

                val ribbon = personalListComposer.composeStatuses(ribbonRaw)

                if (ribbon.isEmpty()) {
                    _personalListStateFlow.value = _personalListStateFlow.value.copy(
                        placeholder = Placeholder.Ribbon.Empty,
                        isPullDownRefreshing = false,
                    )
                } else {
                    if (currentRibbonId == null) {
                        currentRibbonId = ribbon[0].id
                    }

                    if (ribbon.none { it.id == currentRibbonId }) {
                        currentRibbonId = ribbon.first().id
                    }

                    _personalListStateFlow.value = _personalListStateFlow.value.copy(
                        ribbon = ribbon,
                        currentRibbonId = currentRibbonId.orEmpty(),
                        placeholder = Placeholder.None,
                    )
                    currentRibbonId?.let { status ->
                        fetchCategory(status = status)
                    }
                }
            },
            onFailure = {
                logInfo("Error during initial loading of state of personal list", it)
                _personalListStateFlow.update { state ->
                    state.copy(
                        placeholder = Placeholder.Ribbon.Error,
                    )
                }
            }
        )
    }

    fun onScrolled() {
        if (personalListStateFlow.value.isScrollNeed) {
            _personalListStateFlow.update {
                _personalListStateFlow.value.copy(isScrollNeed = false)
            }
        }
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            when (action) {
                is Action.EditRateClicked -> {
                    onEditRateClicked(action.editRateId)
                }

                is Action.UserRateIncrement -> {
                    onUserRateIncrement(action.editRateId)
                }

                else -> {
                    actionConsumer.consume(action)
                }
            }
        }
    }

    fun onRibbonItemSelected(id: String) {
        if (_personalListStateFlow.value.currentRibbonId == id) {
            return
        }
        currentRibbonId = id

        _personalListStateFlow.value = _personalListStateFlow.value.copy(
            currentRibbonId = id,
            items = listOf(),
        )
        launchOnIo {
            fetchCategory(status = id)
        }
    }

    fun onSortChanged(sort: String) {
        launchOnIo {
            personalListFilterRepository.setSort(sort)

            val uiItems = personalListComposer.compose(
                filter = personalListFilterRepository.getFilter(),
                entityList = userRatesList,
                query = _queryFlow.value,
            )

            _personalListStateFlow.value = _personalListStateFlow.value.copy(
                items = uiItems,
                isScrollNeed = true,
                currentSortId = sort,
            )
        }
    }

    fun onQueryChanged(query: String) {
        _queryFlow.update { query }
    }

    private fun onEditRateClicked(editRateId: Long) {
        userRatesList
            .firstOrNull { it.id == editRateId }
            ?.let {
                onEventReceive(
                    NavigateToEditRate(
                        rateId = editRateId,
                        title = it.anime?.russian.orEmpty(),
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
                onInitialLoad()
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
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
        }
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
                    query = _queryFlow.value,
                )

                _personalListStateFlow.value = _personalListStateFlow.value.copy(
                    items = uiItems,
                    placeholder = if (uiItems.isEmpty()) Placeholder.UserRate.Empty else Placeholder.None,
                )
            },
            onFailure = {
                logInfo("Error during fetch of category with status: $status of personal list", it)
                _personalListStateFlow.value = _personalListStateFlow.value.copy(
                    placeholder = Placeholder.UserRate.Error,
                )
            }
        )
    }

    private fun onUserRateIncrement(editRateId: Long) {
        makeRequest(
            action = {
                userRatesRepository.incrementUserRate(
                    rateId = editRateId,
                )
            },
            onLoading = { isLoading ->
                _personalListStateFlow.update {
                    it.copy(isPullDownRefreshing = isLoading)
                }
            },
            onSuccess = {
                onEditSuccessMessage()
                onInitialLoad()
            },
            onFailure = {
                logInfo("Error during user rate changes of personal list(increment)", it)

                onEditErrorMessage()
            }
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
