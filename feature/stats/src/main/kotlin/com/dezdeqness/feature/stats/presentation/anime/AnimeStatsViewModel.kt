package com.dezdeqness.feature.stats.presentation.anime

import androidx.lifecycle.viewModelScope
import com.dezdeqness.feature.stats.presentation.StatsState
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

class AnimeStatsViewModel @Inject constructor(
    @Named("animeStatsArguments") arguments: AnimeStatsArguments,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
    animeStatsComposer: AnimeStatsComposer,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "AnimeStatsViewModel"

    val statsStateFlow: StateFlow<StatsState> =
        flow {
            emit(StatsState(items = animeStatsComposer.compose(arguments = arguments)))
        }
            .catch {
                logInfo("Error in anime stats flow", it)
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

data class AnimeStatsArguments(
    val scoresArgument: List<AnimeStatsTransferModel>,
    val statusesArgument: List<AnimeStatsTransferModel>,
)
