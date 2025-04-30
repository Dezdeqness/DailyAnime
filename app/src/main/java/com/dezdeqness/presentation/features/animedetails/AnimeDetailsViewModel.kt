package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.event.NavigateToAnimeStats
import com.dezdeqness.presentation.event.NavigateToChronology
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.event.NavigateToScreenshotViewer
import com.dezdeqness.presentation.event.NavigateToSimilar
import com.dezdeqness.presentation.event.ShareUrl
import com.dezdeqness.presentation.features.userrate.EditRateUiModel
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
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
), BaseViewModel.InitialLoaded {

    private val _animeDetailsStateFlow: MutableStateFlow<AnimeDetailsState> =
        MutableStateFlow(AnimeDetailsState())
    val animeDetailsStateFlow: StateFlow<AnimeDetailsState> get() = _animeDetailsStateFlow

    private var animeDetails: AnimeDetailsFullEntity? = null

    init {
        actionConsumer.attachListener(this)
        initialLoad()
    }

    override val viewModelTag = "AnimeDetailsViewModel"

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {}

    override fun onCleared() {
        super.onCleared()
        actionConsumer.detachListener()
    }

    fun onActionReceive(action: Action) {
        launchOnIo {
            when (action) {
                is Action.ScreenShotClick -> {
                    onScreenShotClicked(action.screenshotUrl)
                }
                is Action.StatsClicked -> {
                    onStatsClicked()
                }
                is Action.SimilarClicked -> {
                    onSimilarClicked()
                }
                is Action.ChronologyClicked -> {
                    onChronologyClicked()
                }
                else -> {
                    actionConsumer.consume(action)
                }
            }
        }
    }

    fun onEditRateClicked() {
        val rateId = animeDetails?.animeDetailsEntity?.userRate?.id ?: -1
        val title = animeDetails?.animeDetailsEntity?.russian ?: ""
        val overallEpisodes = animeDetails?.animeDetailsEntity?.episodes ?: -1
        onEventReceive(
            NavigateToEditRate(
                rateId = rateId,
                title = title,
                overallEpisodes = overallEpisodes,
            )
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
                    comment = userRate.comment,
                )
                    .onSuccess { result ->
                        animeDetails?.let { details ->
                            animeDetails = details.copy(
                                animeDetailsEntity = details.animeDetailsEntity.copy(
                                    userRate = result
                                )
                            )
                        }

                        if (userRate.isUserRateExist) {
                            onEditUpdateSuccessMessage()
                        } else {
                            onEditCreateSuccessMessage()
                        }
                    }
                    .onFailure {
                        logInfo("Something wrong with user rate changes", it)
                        onEditErrorMessage()
                    }
            }
        }
    }

    fun onShareButtonClicked() {
        onEventReceive(
            ShareUrl(
                url = animeDetails?.animeDetailsEntity?.url.orEmpty()
            )
        )
    }

    fun onStatsClicked() {
        val details = animeDetails?.animeDetailsEntity ?: return

        onEventReceive(
            NavigateToAnimeStats(
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

        onEventReceive(NavigateToSimilar(animeId = details.id))
    }

    fun onChronologyClicked() {
        val details = animeDetails?.animeDetailsEntity ?: return

        onEventReceive(NavigateToChronology(animeId = details.id))
    }

    fun onRetryButtonClicked() {
        if (animeDetailsStateFlow.value.status == DetailsStatus.Loading) {
            return
        }
        initialLoad()
    }

    fun onScreenShotClicked(previewUrl: String) {
        animeDetails
            ?.screenshots
            ?.indexOfFirst { previewUrl.contains(it.preview) }
            ?.let { index ->
                val list =
                    animeDetails
                        ?.screenshots
                        ?.map { it.original }
                        .orEmpty()

                onEventReceive(
                    NavigateToScreenshotViewer(
                        currentIndex = index,
                        screenshots = list,
                    )
                )
            }
    }

    private fun onEditErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getAnimeEditRateErrorMessage())
        }
    }

    private fun initialLoad() {
        _animeDetailsStateFlow.update { it.copy(status = DetailsStatus.Loading) }
        onInitialLoad(
            collector = flow { emit(getAnimeDetailsUseCase.invoke(animeId)) },
            onSuccess = { details ->
                animeDetails = details
                val isAuthorized = accountRepository.isAuthorized()
                val uiItems = animeDetailsComposer.compose(details)
                _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
                    title = details.animeDetailsEntity.russian,
                    uiModels = uiItems,
                    isEditRateFabShown = isAuthorized,
                    status = DetailsStatus.Loaded,
                )
            },
            onFailure = {
                logInfo("Error during initial load of details with $animeId", it)

                _animeDetailsStateFlow.value = _animeDetailsStateFlow.value.copy(
                    isEditRateFabShown = false,
                    status = DetailsStatus.Error,
                )
            }
        )
    }

    private fun onEditCreateSuccessMessage() {
        launchOnIo {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditCreateSuccessMessage())
        }
    }

    private fun onEditUpdateSuccessMessage() {
        launchOnIo {
            messageConsumer.onSuccessMessage(messageProvider.getAnimeEditUpdateSuccessMessage())
        }
    }

}
