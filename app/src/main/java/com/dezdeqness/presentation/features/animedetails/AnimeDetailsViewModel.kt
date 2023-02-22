package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.NavigateToEditRate
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
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
        launchOnIo {
            onInitialLoad(
                action = { getAnimeDetailsUseCase.invoke(animeId) },
                onSuccess = { details ->
                    animeDetails = details
                    val state = animeDetailsComposer.compose(details)
                    val isAuthorized = accountRepository.isAuthorized()
                    _animeDetailsStateFlow.value = state.copy(
                        isEditUserRateShowing = isAuthorized,
                    )

                    logInfo(message = details.toString())
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
        // TODO: shimmer?
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
            }
        }
    }

}
