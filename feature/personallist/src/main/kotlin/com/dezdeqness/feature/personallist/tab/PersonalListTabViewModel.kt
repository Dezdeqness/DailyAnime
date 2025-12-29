package com.dezdeqness.feature.personallist.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.di.AssistedViewModelFactory
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.feature.personallist.PersonalListComposer
import com.dezdeqness.feature.personallist.Placeholder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

object StatusIdKey : CreationExtras.Key<String>

class PersonalListTabViewModel(
    private val userRatesRepository: UserRatesRepository,
    private val personalListComposer: PersonalListComposer,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
    private val statusId: String,
) : ViewModel() {

    private val viewModelTag = "PersonalListTabViewModel"

    private var currentSortId: String = Sort.NAME.sort

    private var userRatesList: List<UserRateEntity> = listOf()

    private var workerJob: Job? = null

    private val _stateFlow: MutableStateFlow<PersonalListTabState> =
        MutableStateFlow(PersonalListTabState(statusId = statusId))
    val stateFlow: StateFlow<PersonalListTabState> get() = _stateFlow

    fun setSort(sortId: String) {
        if (currentSortId == sortId) return
        currentSortId = sortId
        applySortAndUpdateUi(isScrollNeed = true)
    }

    fun onInitialLoad() {
        fetchCategory(status = statusId, onlyRemote = false)
    }

    fun refreshRemote() {
        fetchCategory(status = statusId, onlyRemote = true)
    }

    fun onPullDownRefreshed() {
        refreshRemote()
    }

    fun onScrolled() {
        if (_stateFlow.value.isScrollNeed) {
            _stateFlow.update { it.copy(isScrollNeed = false) }
        }
    }

    fun getUserRateTitle(userRateId: Long): String? {
        return userRatesList.firstOrNull { it.id == userRateId }?.anime?.russian
    }

    fun onUserRateIncrement(userRateId: Long) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            _stateFlow.update { it.copy(isPullDownRefreshing = true) }
            userRatesRepository.incrementUserRate(rateId = userRateId)
                .onSuccess {
                    messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
                    refreshRemote()
                }
                .onFailure { throwable ->
                    appLogger.logInfo(
                        tag = viewModelTag,
                        message = "Error during user rate increment of personal list",
                        throwable = throwable,
                    )
                    messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
                    _stateFlow.update {
                        it.copy(
                            isPullDownRefreshing = false,
                            placeholder = Placeholder.UserRate.Error
                        )
                    }
                }
        }
    }

    private fun fetchCategory(status: String, onlyRemote: Boolean) {
        workerJob?.cancel()
        workerJob = viewModelScope.launch(coroutineDispatcherProvider.io()) {
            _stateFlow.update { it.copyLoading(isVisible = true) }
            userRatesRepository.getUserRates(status = status, page = 1, onlyRemote = onlyRemote)
                .collect { result ->
                    result
                        .onSuccess { list ->
                            userRatesList = list
                            applySortAndUpdateUi(isScrollNeed = false)
                        }
                        .onFailure { throwable ->
                            appLogger.logInfo(
                                tag = viewModelTag,
                                message = "Error during fetch of category with status: $status of personal list",
                                throwable = throwable,
                            )
                            _stateFlow.update {
                                it.copyLoading(isVisible = false).copy(
                                    placeholder = Placeholder.UserRate.Error,
                                    isPullDownRefreshing = false,
                                )
                            }
                        }
                }
        }
    }

    private fun applySortAndUpdateUi(isScrollNeed: Boolean) {
        val sorted = when (currentSortId) {
            Sort.NAME.sort -> userRatesList.sortedBy { it.anime?.russian.orEmpty() }
            Sort.PROGRESS.sort -> userRatesList.sortedByDescending { it.episodes }
            Sort.SCORE.sort -> userRatesList.sortedByDescending { it.score }
            Sort.EPISODES.sort -> userRatesList.sortedByDescending { item ->
                item.anime?.episodes?.toLong() ?: 0L
            }

            else -> userRatesList
        }

        val uiItems = personalListComposer.compose(entityList = sorted)
        _stateFlow.update { state ->
            state.copyLoading(isVisible = false).copy(
                items = uiItems,
                placeholder = if (uiItems.isEmpty()) Placeholder.UserRate.Empty else Placeholder.None,
                isPullDownRefreshing = false,
                isScrollNeed = isScrollNeed,
            )
        }
    }

    class Factory @Inject constructor(
        private val userRatesRepository: UserRatesRepository,
        private val personalListComposer: PersonalListComposer,
        private val messageConsumer: MessageConsumer,
        private val messageProvider: BaseMessageProvider,
        private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
        private val appLogger: AppLogger,
    ) : AssistedViewModelFactory<PersonalListTabViewModel> {

        override fun create(extras: CreationExtras): PersonalListTabViewModel {
            val statusId = extras[StatusIdKey].orEmpty()
            return PersonalListTabViewModel(
                statusId = statusId,
                userRatesRepository = userRatesRepository,
                personalListComposer = personalListComposer,
                messageConsumer = messageConsumer,
                messageProvider = messageProvider,
                coroutineDispatcherProvider = coroutineDispatcherProvider,
                appLogger = appLogger,
            )
        }
    }
}
