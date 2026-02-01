package com.dezdeqness.feature.personallist.tab.store

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.repository.UserRatesRepository
import com.dezdeqness.domain.usecases.GetPersonalListByStatusUseCase
import com.dezdeqness.feature.personallist.tab.PersonalListComposer
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class PersonalListActor @Inject constructor(
    private val useCase: GetPersonalListByStatusUseCase,
    private val personalListComposer: PersonalListComposer,
    private val userRatesRepository: UserRatesRepository,
    private val appLogger: AppLogger,
) : Actor<PersonalListNamespace.Command, PersonalListNamespace.Event>() {

    override fun execute(command: PersonalListNamespace.Command) =
        when (command) {
            is PersonalListNamespace.Command.LoadPage ->
                flow {
                    val state = useCase.invoke(
                        status = command.status,
                        page = command.page,
                    )
                    state.fold(
                        onSuccess = { data ->
                            val items = personalListComposer.compose(data.list)
                            if (command.isLoadMore) {
                                emit(
                                    PersonalListNamespace.Event.OnLoadMorePageLoaded(
                                        list = items,
                                        hasNextPage = data.hasNextPage
                                    )
                                )
                            } else {
                                emit(
                                    PersonalListNamespace.Event.OnPageLoaded(
                                        list = items,
                                        hasNextPage = data.hasNextPage,
                                    )
                                )
                            }
                        },
                        onFailure = { e ->
                            val message: String
                            val event = if (command.isLoadMore) {
                                message =
                                    "Error during load more of personal list with such status: ${command.status}, page: ${command.page}"
                                PersonalListNamespace.Event.OnLoadMorePageError(message, e)
                            } else {
                                message =
                                    "Error during initial loading of state of personal list with such status: ${command.status},"
                                PersonalListNamespace.Event.OnLoadPageError(message, e)
                            }

                            appLogger.logInfo(TAG, message, e)
                            emit(event)
                        }
                    )
                }

            is PersonalListNamespace.Command.IncrementUserRate -> flow {
                userRatesRepository.incrementUserRate(
                    rateId = command.userRateId,
                )
                    .onSuccess { userRate ->
                        emit(PersonalListNamespace.Event.EditUserRateSuccess)

                        val statusChanged = command.statusId != userRate.status

                        if (statusChanged) {
                            emit(PersonalListNamespace.Event.ItemRemovedLocally(userRate.id))
                        } else {
                            personalListComposer.convertOnlyUserAnime(userRate)?.let { item ->
                                emit(PersonalListNamespace.Event.UpdateUserRateLocally(item))
                            }
                        }
                    }
                    .onFailure { throwable ->
                        appLogger.logInfo(
                            TAG,
                            "Error during user rate changes of personal list",
                            throwable
                        )

                        emit(PersonalListNamespace.Event.EditUserRateError)
                    }
            }

            is PersonalListNamespace.Command.UpdateUserRate -> flow {
                val userRate = command.userRate
                userRatesRepository.updateUserRate(
                    rateId = userRate.rateId,
                    status = userRate.status,
                    episodes = userRate.episodes,
                    score = userRate.score,
                    comment = userRate.comment,
                )
                    .onSuccess {
                        emit(PersonalListNamespace.Event.EditUserRateSuccess)

                        val statusChanged = command.statusId != userRate.status

                        if (statusChanged) {
                            emit(PersonalListNamespace.Event.ItemRemovedLocally(userRate.rateId))
                        } else {
                            personalListComposer.convertOnlyUserAnime(it)?.let { item ->
                                emit(PersonalListNamespace.Event.UpdateUserRateLocally(item))
                            }
                        }
                    }
                    .onFailure { throwable ->
                        appLogger.logInfo(
                            TAG,
                            "Error during user rate changes of personal list",
                            throwable
                        )

                        emit(PersonalListNamespace.Event.EditUserRateError)
                    }
            }
        }

    companion object {
        const val TAG = "HistoryActor"
    }
}
