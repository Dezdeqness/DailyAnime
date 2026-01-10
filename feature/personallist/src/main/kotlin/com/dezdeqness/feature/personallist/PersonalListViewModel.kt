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
import com.dezdeqness.domain.repository.PersonalListFilterRepository
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.feature.userrate.EditRateUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonalListViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val personalListFilterRepository: PersonalListFilterRepository,
    private val userRepository: UserRepository,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val settingsRepository: SettingsRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
) : ViewModel() {

    private var ribbonRaw: FullAnimeStatusesEntity = FullAnimeStatusesEntity(listOf())

    private val _pagerStateFlow: MutableStateFlow<PersonalListPagerState> =
        MutableStateFlow(PersonalListPagerState())
    val pagerStateFlow: StateFlow<PersonalListPagerState> get() = _pagerStateFlow

    private val _bottomSheetFlow: MutableStateFlow<BottomSheet> = MutableStateFlow(BottomSheet.None)
    val bottomSheetFlow: StateFlow<BottomSheet> get() = _bottomSheetFlow

    private val _refreshStatusFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val refreshStatusFlow: Flow<String> = _refreshStatusFlow.asSharedFlow()

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            settingsRepository
                .observePreference(StatusesOrderPreference)
                .drop(1)
                .collect {
                    val ribbon = personalListComposer.composeStatuses(ribbonRaw)
                    _pagerStateFlow.update { state ->
                        val selectedId = state.selectedRibbonId
                        state.copy(
                            ribbon = ribbon,
                            selectedRibbonId = selectedId.takeIf { id -> ribbon.any { it.id == id } }
                                ?: ribbon.firstOrNull()?.id.orEmpty(),
                        )
                    }
                }
        }
    }

    private val viewModelTag = "PersonalListViewModel"

    // TODO: Add fetching when app from foreground
    fun onInitialLoad() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            val sortId = personalListFilterRepository.getFilter().sort.sort
            _pagerStateFlow.update { it.copy(currentSortId = sortId) }

            _pagerStateFlow.update { it.copy(isInitialLoading = true) }
            userRepository.getProfileDetails().collect { result ->
                result
                    .onSuccess { details ->
                        ribbonRaw = details.fullAnimeStatusesEntity

                        val ribbon = personalListComposer.composeStatuses(ribbonRaw)
                        if (ribbon.isEmpty()) {
                            _pagerStateFlow.update { state ->
                                state.copy(
                                    ribbon = listOf(),
                                    selectedRibbonId = "",
                                    isInitialLoading = false,
                                    placeholder = Placeholder.Ribbon.Empty,
                                )
                            }
                        } else {
                            _pagerStateFlow.update { state ->
                                val selected =
                                    state.selectedRibbonId.takeIf { id -> ribbon.any { it.id == id } }
                                        ?: ribbon.first().id

                                state.copy(
                                    ribbon = ribbon,
                                    selectedRibbonId = selected,
                                    isInitialLoading = false,
                                    placeholder = Placeholder.None,
                                )
                            }
                        }
                    }
                    .onFailure { throwable ->
                        logInfo("Error during initial loading of state of personal list", throwable)
                        _pagerStateFlow.update { state ->
                            state.copy(
                                isInitialLoading = false,
                                placeholder = Placeholder.Ribbon.Error,
                            )
                        }
                    }
            }
        }
    }

    fun onRibbonItemSelected(id: String) {
        if (_pagerStateFlow.value.selectedRibbonId == id) return
        _pagerStateFlow.update { it.copy(selectedRibbonId = id) }
    }

    fun onSortChanged(sort: String) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            personalListFilterRepository.setSort(sort)
            _pagerStateFlow.update { it.copy(currentSortId = sort) }
        }
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

}
