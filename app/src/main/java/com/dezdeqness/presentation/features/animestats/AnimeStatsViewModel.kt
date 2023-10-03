package com.dezdeqness.presentation.features.animestats

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.features.animedetails.AnimeStatsTransferModel
import com.dezdeqness.presentation.features.stats.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class AnimeStatsViewModel @Inject constructor(
    @Named("animeStatsArguments") private val arguments: AnimeStatsArguments,
    animeStatsComposer: AnimeStatsComposer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _statsStateFlow: MutableStateFlow<AnimeStatsState> =
        MutableStateFlow(AnimeStatsState())
    val statsStateFlow: StateFlow<AnimeStatsState> get() = _statsStateFlow

    init {
        val uiItems = animeStatsComposer.compose(animeStatsFragment = arguments)
        _statsStateFlow.value = _statsStateFlow.value.copy(
            items = uiItems,
        )
    }

    override val viewModelTag = "AnimeStatsViewModel"

    override fun onEventConsumed(event: Event) {}

}

data class AnimeStatsArguments(
    val scoresArgument: List<AnimeStatsTransferModel>,
    val statusesArgument: List<AnimeStatsTransferModel>,
)
