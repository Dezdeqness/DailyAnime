package com.dezdeqness.feature.stats.presentation.anime

import app.cash.turbine.test
import com.dezdeqness.feature.stats.presentation.StatsState
import com.dezdeqness.feature.stats.presentation.models.StatsHeaderUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeStatsViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var animeStatsComposer: AnimeStatsComposer

    private val arguments = AnimeStatsArguments(
        scoresArgument = listOf(AnimeStatsTransferModel(name = "10", value = 154)),
        statusesArgument = listOf(AnimeStatsTransferModel(name = "watching", value = 12)),
    )

    private lateinit var viewModel: AnimeStatsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = AnimeStatsViewModel(
            arguments = arguments,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
            animeStatsComposer = animeStatsComposer,
        )

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN composer returns items SHOULD emit state with items`() = runTest {
        val items = listOf(StatsHeaderUiModel(header = "Scores"))

        every { animeStatsComposer.compose(arguments) } returns items

        viewModel.statsStateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(StatsState(), initial)

            val loaded = awaitItem()
            assertEquals(StatsState(items = items), loaded)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN composer throws exception SHOULD keep empty state and log error`() = runTest {
        val error = RuntimeException("Compose error")

        every { animeStatsComposer.compose(arguments) } throws error

        viewModel.statsStateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(StatsState(), initial)

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }

        verify { logger.logInfo("AnimeStatsViewModel", any(), error) }
    }
}
