package com.dezdeqness.feature.achievements.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.achievements.repository.AchievementRepository
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class AchievementsViewModel @Inject constructor(
    @Named("userId") private val userId: Long,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
    private val achievementRepository: AchievementRepository,
    private val configurationProvider: ConfigurationProvider,
    private val achievementsComposer: AchievementsComposer,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "AchievementsViewModel"

    val achievementsState: StateFlow<AchievementsUiState> =
        flow {
            emit(AchievementsUiState(status = Status.Loading))

            val config = configurationProvider.getAchievementConfig()
            val result = achievementRepository.fetchAchievementsByUserId(id = userId)

            result
                .onSuccess { userAchievements ->
                    val common = config.common.let { cfg ->
                        achievementsComposer.compose(cfg, userAchievements)
                    }

                    val genres = config.genres.let { cfg ->
                        achievementsComposer.compose(cfg, userAchievements)
                    }

                    emit(
                        AchievementsUiState(
                            status = if (common.isEmpty() && genres.isEmpty()) {
                                Status.Empty
                            } else {
                                Status.Loaded
                            },
                            common = common,
                            genres = genres,
                        ),
                    )
                }
                .onFailure {
                    logInfo("Error fetching achievements", it)
                    emit(AchievementsUiState(status = Status.Error))
                }
        }
            .catch {
                logInfo("Error in achievements flow", it)
                emit(AchievementsUiState(status = Status.Error))
            }
            .flowOn(coroutineDispatcherProvider.io())
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = AchievementsUiState(status = Status.Initial),
            )
}
