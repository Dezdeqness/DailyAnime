package com.dezdeqness.feature.details.anime.presentation.store

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.feature.details.anime.presentation.composer.AnimeDetailsComposer
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.Logger
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class AnimeDetailsActor @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val createOrUpdateUserRateUseCase: CreateOrUpdateUserRateUseCase,
    private val authRepository: AuthRepository,
    private val composer: AnimeDetailsComposer,
    private val logger: Logger,
) : Actor<AnimeDetailsNamespace.Command, AnimeDetailsNamespace.Event>() {

    override fun execute(command: AnimeDetailsNamespace.Command) =
        when (command) {
            is AnimeDetailsNamespace.Command.Base -> when (val base = command.command) {
                is BaseDetailsCommand.LoadDetails -> flow {
                    try {
                        val result = getAnimeDetailsUseCase.invoke(base.id)
                        val details = result.getOrThrow()
                        val isAuthorized = authRepository.isAuthorized()
                        emit(
                            AnimeDetailsNamespace.Event.OnDetailsLoaded(
                                details = details,
                                sections = composer.compose(details),
                                isAuthorized = isAuthorized,
                            )
                        )
                    } catch (error: Throwable) {
                        val message = "Error during loading anime details, id: ${base.id}"
                        logger.logInfo(TAG, message, error)
                        emit(
                            AnimeDetailsNamespace.Event.Base(
                                BaseDetailsEvent.OnDetailsLoadError(message, error),
                            )
                        )
                    }
                }
            }

            is AnimeDetailsNamespace.Command.CreateOrUpdateUserRate -> flow {
                try {
                    val isCreate = !command.model.isUserRateExist
                    val rate = createOrUpdateUserRateUseCase.invoke(
                        rateId = command.model.rateId,
                        targetId = command.animeId.toString(),
                        status = command.model.status,
                        episodes = command.model.episodes,
                        score = command.model.score,
                        comment = command.model.comment,
                    ).getOrThrow()
                    emit(AnimeDetailsNamespace.Event.OnUserRateSaved(isCreate = isCreate, rate = rate))
                } catch (error: Throwable) {
                    logger.logInfo(TAG, "Error during edit user rate", error)
                    emit(AnimeDetailsNamespace.Event.OnUserRateSaveError)
                }
            }
        }

    companion object {
        private const val TAG = "AnimeDetailsActor"
    }
}
