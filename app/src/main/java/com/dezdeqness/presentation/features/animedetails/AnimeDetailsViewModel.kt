package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.EventListener
import com.dezdeqness.presentation.event.NavigateToAnimeState
import com.dezdeqness.presentation.event.NavigateToChronology
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.NavigateToSimilar
import com.dezdeqness.presentation.event.ShareUrl
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class AnimeDetailsViewModel @Inject constructor(
    @Named("animeId") private val animeId: Long,
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeDetailsComposer: AnimeDetailsComposer,
    private val createOrUpdateUserRateUseCase: CreateOrUpdateUserRateUseCase,
    private val accountRepository: AccountRepository,
    private val actionConsumer: ActionConsumer,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded, EventListener {

    private val _animeDetailsStateFlow: MutableStateFlow<AnimeDetailsState> =
        MutableStateFlow(AnimeDetailsState())
    val animeDetailsStateFlow: StateFlow<AnimeDetailsState> get() = _animeDetailsStateFlow

    private var animeDetails: AnimeDetailsFullEntity? = null

    init {
        actionConsumer.attachListener(this)
        launchOnIo {
            onInitialLoad(
                action = { getAnimeDetailsUseCase.invoke(animeId) },
                onSuccess = { details ->
                    animeDetails = details
                    val isAuthorized = accountRepository.isAuthorized()
                    val uiItems = animeDetailsComposer.compose(details)
                    _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
                        title = details.animeDetailsEntity.russian,
                        uiModels = uiItems,
                        isEditRateFabShown = isAuthorized,
                    )

                    logInfo(message = details.toString())
                },
                onFailure = {
                    _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
                        isEditRateFabShown = false,
                    )
                }
            )
        }
    }

    override fun viewModelTag() = "ItemDetailsViewModel"

    override fun onEventConsumed(event: Event) {
        val value = _animeDetailsStateFlow.value
        _animeDetailsStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    override fun onEventReceive(event: Event) {
        val events = _animeDetailsStateFlow.value.events

        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            events = events + event,
        )
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            actionConsumer.consume(action)
        }
    }

    fun onEditRateClicked() {
        val events = _animeDetailsStateFlow.value.events

        val rateId = animeDetails?.animeDetailsEntity?.userRate?.id ?: -1
        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            events = events + NavigateToEditRate(
                rateId = rateId,
            ),
        )
    }

    fun onUserRateChanged(userRate: EditRateUiModel?) {
        launchOnIo {
            userRate?.let {
                createOrUpdateUserRateUseCase.invoke(
                    rateId = userRate.rateId,
                    targetId = animeId.toString(),
                    status = userRate.status,
                    episodes = userRate.episodes,
                    score = userRate.score,
                )
                    .onSuccess {
                        if (userRate.rateId.toInt() == -1) {
                            onEditCreateSuccessMessage()
                        } else {
                            onEditUpdateSuccessMessage()
                        }
                    }
                    .onFailure {
                        onEditErrorMessage()
                    }
            }
        }
    }

    fun onShareButtonClicked() {
        val events = _animeDetailsStateFlow.value.events

        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            events = events + ShareUrl(
                url = animeDetails?.animeDetailsEntity?.url.orEmpty()
            )
        )
    }

    fun onToolbarVisibilityOn() {
        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            isToolbarVisible = true
        )
    }

    fun onToolbarVisibilityOff() {
        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            isToolbarVisible = false
        )
    }

    fun onStatsClicked() {
        val details = animeDetails?.animeDetailsEntity ?: return
        val events = _animeDetailsStateFlow.value.events

        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            events = events + NavigateToAnimeState(
                scoreList = details.scoresStats.map { score ->
                    AnimeStatsTransferModel(
                        score.name,
                        score.value,
                    )
                },
                statusesList = details.statusesStats.map { status ->
                    AnimeStatsTransferModel(
                        status.name,
                        status.value,
                    )
                },
            )
        )
    }

    fun onSimilarClicked() {
        val details = animeDetails?.animeDetailsEntity ?: return

        val events = _animeDetailsStateFlow.value.events

        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            events = events + NavigateToSimilar(animeId = details.id),
        )
    }

    fun onChronologyClicked() {
        val details = animeDetails?.animeDetailsEntity ?: return

        val events = _animeDetailsStateFlow.value.events

        _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
            events = events + NavigateToChronology(animeId = details.id),
        )
    }

    private fun onEditErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
        }
    }

    private fun onEditCreateSuccessMessage() {
        launchOnIo {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
        }
    }

    private fun onEditUpdateSuccessMessage() {
        launchOnIo {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditCreateSuccessMessage())
        }
    }

}
