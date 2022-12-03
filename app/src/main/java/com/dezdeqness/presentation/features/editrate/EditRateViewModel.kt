package com.dezdeqness.presentation.features.editrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.model.UserRateStatusEntity
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.presentation.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class EditRateViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    @Named("rateId") private val rateId: Long,
) : ViewModel() {
    private val _editRateStateFlow: MutableStateFlow<EditRateState> =
        MutableStateFlow(EditRateState())

    val editRateStateFlow: StateFlow<EditRateState> get() = _editRateStateFlow

    private var localUserRate: UserRateEntity? = null

    init {
        fetchUserRate()
    }

    fun onRatingChanged(rating: Long) {
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            score = rating,
        )

        checkIsContentChanged()

    }

    fun onEpisodesMinusClicked() {
        val episode = _editRateStateFlow.value.episode - 1
        if (episode < 0) {
            return
        }

        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            episode = episode,
        )

        checkIsContentChanged()
    }

    fun onEpisodesPlusClicked() {
        val episode = _editRateStateFlow.value.episode + 1

        _editRateStateFlow.value = _editRateStateFlow.value.copy(
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

    fun onStatusChanged(status: UserRateStatusEntity) {
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            status = status,
        )

        checkIsContentChanged()
    }

    fun onApplyButtonClicked() {
        if (_editRateStateFlow.value.isUserRateChanged) {
            val uiModel = EditRateUiModel(
                rateId = _editRateStateFlow.value.rateId,
                status = _editRateStateFlow.value.status.status,
                episodes = _editRateStateFlow.value.episode,
                score = _editRateStateFlow.value.score.toFloat(),
            )
            val events = _editRateStateFlow.value.events

            _editRateStateFlow.value = _editRateStateFlow.value.copy(
                events = events + Event.EditUserRate(
                    userRateUiModel = uiModel,
                ),
            )
        }
    }

    fun onEventConsumed(event: Event) {
        val value = _editRateStateFlow.value
        _editRateStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    private fun checkIsContentChanged() {
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            isUserRateChanged = isUserRateChanged(),
        )
    }

    private fun fetchUserRate() {
        viewModelScope.launch(Dispatchers.IO) {
            userRatesRepository.getLocalUserRate(rateId = rateId)?.let { userRate ->
                localUserRate = userRate
                emitEditRateState(userRate)
            }
        }
    }

    private fun emitEditRateState(userRate: UserRateEntity, isUserRateChanged: Boolean = false) {
        val title = userRate.anime?.russian.orEmpty()
        val status = UserRateStatusEntity.fromString(userRate.status)
        val score = userRate.score
        val episode = userRate.episodes
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            rateId = userRate.id,
            title = title,
            status = status,
            score = score,
            episode = episode,
            isUserRateChanged = isUserRateChanged,
        )
    }

    private fun isUserRateChanged(): Boolean {
        val uiEditRate = _editRateStateFlow.value
        return uiEditRate.status.status != localUserRate?.status ||
                uiEditRate.episode != localUserRate?.episodes ||
                uiEditRate.score != localUserRate?.score
    }

}
