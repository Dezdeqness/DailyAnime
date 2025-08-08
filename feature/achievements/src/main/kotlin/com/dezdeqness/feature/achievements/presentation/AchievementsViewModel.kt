package com.dezdeqness.feature.achievements.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Named

class AchievementsViewModel @Inject constructor(
    @Named("userId") private val userId: Long,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLogger: AppLogger,
    private val achievementRepository: AchievementRepository,
    private val configurationProvider: ConfigurationProvider,
    private val achievementsComposer: AchievementsComposer,
) : ViewModel() {

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
                            status = if (common.isEmpty() && genres.isEmpty()) Status.Empty else Status.Loaded,
                            common = common,
                            genres = genres
                        )
                    )
                }
                .onFailure {
                    appLogger.logInfo(TAG, throwable = it)
                    emit(AchievementsUiState(status = Status.Error))
                }
        }
            .catch {
                appLogger.logInfo(TAG, throwable = it)
                AchievementsUiState(status = Status.Error)
            }
            .flowOn(coroutineDispatcherProvider.io())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = AchievementsUiState(status = Status.Loading)
            )

    companion object {
        private const val TAG = "AchievementsViewModel"
    }
}
