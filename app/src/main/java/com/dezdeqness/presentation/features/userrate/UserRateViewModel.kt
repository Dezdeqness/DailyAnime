package com.dezdeqness.presentation.features.userrate

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.presentation.event.EditUserRate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject
import javax.inject.Named

class UserRateViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    @Named("rateId") private val rateId: Long,
    @Named("title") private val title: String,
    @Named("overallEpisode") private val overallEpisode: Int,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _userRateStateFlow: MutableStateFlow<UserRateState> =
        MutableStateFlow(UserRateState())

    val userRateStateFlow: StateFlow<UserRateState> get() = _userRateStateFlow

    private var localUserRate: UserRateEntity? = null

    init {
        fetchUserRate()
    }

    override val viewModelTag = "UserRateViewModel"

    fun onScoreChanged(score: Long) {
        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            score = score,
        )

        checkIsContentChanged()
    }

    fun onEpisodesMinusClicked() {
        val episode = _userRateStateFlow.value.episode - 1
        if (episode < 0) {
            return
        }

        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            episode = episode,
        )

        checkIsContentChanged()
    }

    fun onEpisodesPlusClicked() {
        val episode = _userRateStateFlow.value.episode + 1

        if (overallEpisode != 0 && episode > overallEpisode) {
            return
        }

        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            episode = episode,
        )

        checkIsContentChanged()
    }

    fun onResetButtonClicked() {
        localUserRate?.let { userRate ->
            emitEditRateState(
                userRate = userRate,
                isUserRateChanged = false,
            )
        }
    }

    fun onStatusChanged(status: String) {
        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            selectedStatus = status,
        )

        checkIsContentChanged()
    }

    fun onApplyButtonClicked() {
        if (_userRateStateFlow.value.isContentChanged) {
            val model = _userRateStateFlow.value
            val uiModel = EditRateUiModel(
                rateId = model.rateId,
                status = model.selectedStatus,
                episodes = model.episode,
                score = model.score.toFloat(),
                comment = model.comment,
            )
            onEventReceive(EditUserRate(userRateUiModel = uiModel))
        }
    }

    fun onSelectStatusClicked() {
        if (_userRateStateFlow.value.isSelectStatusDialogShowed) {
            return
        }

        _userRateStateFlow.update {
            it.copy(isSelectStatusDialogShowed = true)
        }
    }

    fun onCloseSelectStatusClicked() {
        _userRateStateFlow.update {
            it.copy(isSelectStatusDialogShowed = false)
        }
    }

    fun onCommentChanged(comment: String) {
        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            comment = comment,
        )

        checkIsContentChanged()
    }

    fun onEpisodesChanged(episodes: String) {
        if (episodes.isBlank()) {
            _userRateStateFlow.update {
                it.copy(episode = 0)
            }
        }
        val parsedEpisodes = episodes.toLongOrDefault(_userRateStateFlow.value.episode)

        _userRateStateFlow.update {
            it.copy(episode = parsedEpisodes)
        }
    }

    private fun checkIsContentChanged() {
        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            isContentChanged = isUserRateChanged(),
        )
    }

    private fun fetchUserRate() {
        launchOnIo {
            val userRate = userRatesRepository.getLocalUserRate(rateId = rateId)
            localUserRate = userRate ?: UserRateEntity.EMPTY_USER_RATE

            emitDefaultState()

            userRate?.let {
                emitEditRateState(userRate)
            }
        }
    }

    private fun emitDefaultState() {
        _userRateStateFlow.value = _userRateStateFlow.value.copy(title = title, isEditMode = false)
    }

    private fun emitEditRateState(userRate: UserRateEntity, isUserRateChanged: Boolean = false) {
        val score = userRate.score
        val episode = userRate.episodes

        _userRateStateFlow.value = _userRateStateFlow.value.copy(
            isEditMode = userRate.isEmptyUserRate().not(),
            rateId = userRate.id,
            title = title,
            selectedStatus = userRate.status,
            score = score,
            episode = episode,
            isContentChanged = isUserRateChanged,
            comment = userRate.text
        )
    }

    private fun isUserRateChanged(): Boolean {
        val uiEditRate = _userRateStateFlow.value
        return uiEditRate.selectedStatus != localUserRate?.status ||
                uiEditRate.episode != localUserRate?.episodes ||
                uiEditRate.score != localUserRate?.score ||
                uiEditRate.comment != localUserRate?.text
    }

}
