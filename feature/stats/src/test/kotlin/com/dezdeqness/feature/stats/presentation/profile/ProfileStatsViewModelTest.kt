package com.dezdeqness.feature.stats.presentation.profile

import app.cash.turbine.test
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.feature.stats.presentation.StatsState
import com.dezdeqness.feature.stats.presentation.models.StatsHeaderUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
class ProfileStatsViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var statsComposer: ProfileStatsComposer

    private lateinit var viewModel: ProfileStatsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = ProfileStatsViewModel(
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
            userRepository = userRepository,
            statsComposer = statsComposer,
        )

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN profile exists SHOULD emit state with composed items`() = runTest {
        val account = mockk<AccountEntity>()
        val items = listOf(StatsHeaderUiModel(header = "Types"))

        every { userRepository.getProfileLocal() } returns account
        every { statsComposer.compose(account) } returns items

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
    fun `WHEN profile is missing SHOULD keep empty state`() = runTest {
        every { userRepository.getProfileLocal() } returns null

        viewModel.statsStateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(StatsState(), initial)

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 0) { statsComposer.compose(any()) }
    }

    @Test
    fun `WHEN repository throws exception SHOULD keep empty state and log error`() = runTest {
        val error = RuntimeException("Database error")

        every { userRepository.getProfileLocal() } throws error

        viewModel.statsStateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(StatsState(), initial)

            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }

        verify { logger.logInfo("ProfileStatsViewModel", any(), error) }
    }
}
