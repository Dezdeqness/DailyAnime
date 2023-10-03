package com.dezdeqness.presentation.features.editrate

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.domain.model.UserRateStatusEntity
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.presentation.event.EditUserRate
import com.dezdeqness.presentation.event.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class EditRateViewModel @Inject constructor(
    private val userRatesRepository: UserRatesRepository,
    private val editRateStatusMapper: EditRateStatusMapper,
    @Named("rateId") private val rateId: Long,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _editRateStateFlow: MutableStateFlow<EditRateState> =
        MutableStateFlow(EditRateState())

    val editRateStateFlow: StateFlow<EditRateState> get() = _editRateStateFlow

    private var localUserRate: UserRateEntity? = null

    init {
        fetchUserRate()
    }

    override val viewModelTag = "EditRateViewModel"

    override fun onEventConsumed(event: Event) {
        val value = _editRateStateFlow.value
        _editRateStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    fun onRatingChanged(position: Int) {
        val score = _editRateStateFlow.value.carouselUiModels[position].value
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            score = score,
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

    fun onStatusChanged(statusEntity: UserRateStatusEntity) {
        val modelStatus = editRateStatusMapper.map(status = statusEntity.status)
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            status = modelStatus,
        )

        checkIsContentChanged()
    }

    fun onApplyButtonClicked() {
        if (_editRateStateFlow.value.isUserRateChanged) {
            val uiModel = EditRateUiModel(
                rateId = _editRateStateFlow.value.rateId,
                status = _editRateStateFlow.value.status.id,
                episodes = _editRateStateFlow.value.episode,
                score = _editRateStateFlow.value.score.toFloat(),
            )
            val events = _editRateStateFlow.value.events

            _editRateStateFlow.value = _editRateStateFlow.value.copy(
                events = events + EditUserRate(
                    userRateUiModel = uiModel,
                ),
            )
        }
    }

    private fun checkIsContentChanged() {
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            isUserRateChanged = isUserRateChanged(),
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
        val modelStatus = editRateStatusMapper.map(status = UserRateStatusEntity.UNKNOWN.status)
        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            status = modelStatus,
            carouselUiModels = generateSequence(0L) { it + 1 }
                .take(n = 11)
                .map { CarouselUiModel(value = it) }
                .toList(),
        )
    }

    private fun emitEditRateState(userRate: UserRateEntity, isUserRateChanged: Boolean = false) {
        val title = userRate.anime?.russian.orEmpty()
        val modelStatus = editRateStatusMapper.map(status = userRate.status)
        val score = userRate.score
        val episode = userRate.episodes

        _editRateStateFlow.value = _editRateStateFlow.value.copy(
            rateId = userRate.id,
            title = title,
            status = modelStatus,
            score = score,
            episode = episode,
            isUserRateChanged = isUserRateChanged,
        )
    }

    private fun isUserRateChanged(): Boolean {
        val uiEditRate = _editRateStateFlow.value
        return uiEditRate.status.id != localUserRate?.status ||
                uiEditRate.episode != localUserRate?.episodes ||
                uiEditRate.score != localUserRate?.score
    }

}
