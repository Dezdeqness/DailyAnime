package com.dezdeqness.feature.personallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.settings.models.StatusesOrderPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.user.model.FullAnimeStatusesEntity
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.feature.personallist.PersonalTabsListPagerState.Companion.empty
import com.dezdeqness.feature.personallist.PersonalTabsListPagerState.Companion.loaded
import com.dezdeqness.feature.personallist.PersonalTabsListPagerState.Companion.loading
import com.dezdeqness.feature.personallist.tab.PersonalListComposer
import com.dezdeqness.feature.userrate.EditRateUiModel
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalListTabsViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val userRepository: UserRepository,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val settingsRepository: SettingsRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
) : ViewModel() {

    private val viewModelTag = "PersonalListViewModel"

    private val loadEvents = MutableSharedFlow<LoadEvent>(extraBufferCapacity = 1)

    private var ribbonRaw: FullAnimeStatusesEntity = FullAnimeStatusesEntity(listOf())

    private val _bottomSheetFlow: MutableStateFlow<BottomSheet> = MutableStateFlow(BottomSheet.None)
    val bottomSheetFlow: StateFlow<BottomSheet> get() = _bottomSheetFlow

    private val _refreshStatusFlow = MutableSharedFlow<RefreshEvent>(extraBufferCapacity = 10)
    val refreshStatusFlow: Flow<RefreshEvent> = _refreshStatusFlow.asSharedFlow()

    data class RefreshEvent(
        val statusId: String,
        val userRateId: Long,
        val shouldRemoveLocally: Boolean,
        val shouldMarkPending: Boolean,
    )

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            settingsRepository
                .observePreference(StatusesOrderPreference)
                .drop(1)
                .distinctUntilChanged()
                .map {
                    loadEvents.tryEmit(LoadEvent.RefreshTabSelected)
                }
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily
                )
                .collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val changes: Flow<Change> =
        loadEvents
            .onStart { emit(LoadEvent.Initial) }
            .flatMapLatest { event ->
                when (event) {
                    LoadEvent.Initial,
                    LoadEvent.RefreshTabs -> userRepository.getProfileDetails()
                        .map { result ->
                            result.fold(
                                onSuccess = { account ->
                                    ribbonRaw = account.fullAnimeStatusesEntity
                                    val ribbon = personalListComposer.composeStatuses(ribbonRaw)
                                    Change.TabsLoaded(ribbon)
                                },
                                onFailure = { exception ->
                                    Change.Error(exception)
                                }
                            )
                        }
                        .onStart { emit(Change.Loading) }

                    is LoadEvent.SelectRibbon -> flowOf(
                        Change.RibbonSelected(event.ribbonId)
                    )

                    LoadEvent.RefreshTabSelected -> flowOf(
                        Change.RemapSelected
                    )
                }
            }
            .flowOn(coroutineDispatcherProvider.io())

    val pagerStateFlow: StateFlow<PersonalTabsListPagerState> =
        changes
            .scan(loading()) { previous, change ->
                when (change) {

                    Change.Loading ->
                        previous.loading()

                    is Change.TabsLoaded -> {
                        if (change.ribbon.isEmpty()) {
                            previous.empty()
                        } else {
                            val selectedId =
                                previous.selectedRibbonId
                                    .takeIf { id -> change.ribbon.any { it.id == id } }
                                    ?: change.ribbon.first().id

                            previous.loaded(
                                ribbon = change.ribbon,
                                selectedRibbonId = selectedId
                            )
                        }
                    }

                    is Change.RibbonSelected ->
                        previous.copy(selectedRibbonId = change.ribbonId)

                    Change.RemapSelected -> {
                        val remapped = personalListComposer.composeStatuses(ribbonRaw)

                        previous.copy(ribbon = remapped)
                    }

                    is Change.Error -> {
                        logInfo("Error during user rate changes of personal list", change.exception)
                        PersonalTabsListPagerState.error()
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = loading()
            )

    fun onRibbonItemSelected(id: String) {
        if (pagerStateFlow.value.selectedRibbonId == id) return
        loadEvents.tryEmit(LoadEvent.SelectRibbon(ribbonId = id))
    }

    fun openEditRateBottomSheet(userRateId: Long, title: String) {
        _bottomSheetFlow.value = BottomSheet.EditRate(
            userRateId = userRateId,
            title = title,
        )
    }

    fun onUserRateBottomDialogClosed() {
        _bottomSheetFlow.value = BottomSheet.None
    }

    fun onUserRateChanged(userRate: EditRateUiModel?) {
        if (userRate == null) {
            return
        }
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            userRatesRepository.updateUserRate(
                rateId = userRate.rateId,
                status = userRate.status,
                episodes = userRate.episodes,
                score = userRate.score,
                comment = userRate.comment,
            )
                .onSuccess {
                    onEditSuccessMessage()
                    _bottomSheetFlow.value = BottomSheet.None
                    _refreshStatusFlow.tryEmit(userRate.status)
                }
                .onFailure { throwable ->
                    logInfo("Error during user rate changes of personal list", throwable)
                    onEditErrorMessage()
                }
        }
    }

    private fun onEditErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
        }
    }

    private fun onEditSuccessMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
        }
    }

    private fun logInfo(message: String, throwable: Throwable? = null) {
        if (throwable == null) {
            appLogger.logInfo(
                tag = viewModelTag,
                message = message,
            )
        } else {
            appLogger.logInfo(
                tag = viewModelTag,
                message = message,
                throwable = throwable,
            )
        }
    }

    private sealed class LoadEvent {
        data object Initial : LoadEvent()
        data object RefreshTabs : LoadEvent()
        data object RefreshTabSelected : LoadEvent()
        data class SelectRibbon(val ribbonId: String) : LoadEvent()
    }

    private sealed interface Change {
        data object Loading : Change
        data class TabsLoaded(
            val ribbon: List<RibbonStatusUiModel>,
        ) : Change

        data class RibbonSelected(val ribbonId: String) : Change
        data object RemapSelected : Change

        data class Error(val exception: Throwable) : Change
    }

}
