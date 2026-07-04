package com.dezdeqness.feature.stats.presentation.profile

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.feature.stats.presentation.StatsState
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class ProfileStatsViewModel @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
    private val userRepository: UserRepository,
    private val statsComposer: ProfileStatsComposer,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "ProfileStatsViewModel"

    val statsStateFlow: StateFlow<StatsState> =
        flow {
            userRepository.getProfileLocal()?.let { account ->
                emit(StatsState(items = statsComposer.compose(account)))
            }
        }
            .catch {
                logInfo("Error in profile stats flow", it)
                emit(StatsState())
            }
            .flowOn(coroutineDispatcherProvider.io())
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = StatsState(),
            )
}
