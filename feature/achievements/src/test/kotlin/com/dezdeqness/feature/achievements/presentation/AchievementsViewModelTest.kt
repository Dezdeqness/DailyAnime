package com.dezdeqness.feature.achievements.presentation

import app.cash.turbine.test
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.Logger
import com.dezdeqness.contract.achievements.model.AchievementConfigDataEntity
import com.dezdeqness.contract.achievements.model.AchievementEntity
import com.dezdeqness.contract.achievements.repository.AchievementConfigRepository
import com.dezdeqness.contract.achievements.repository.AchievementRepository
import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class AchievementsViewModelTest {

    @MockK
    private lateinit var achievementRepository: AchievementRepository

    @MockK
    private lateinit var achievementConfigRepository: AchievementConfigRepository

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var achievementsComposer: AchievementsComposer

    private lateinit var viewModel: AchievementsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = AchievementsViewModel(
            userId = USER_ID,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
            achievementRepository = achievementRepository,
            achievementConfigRepository = achievementConfigRepository,
            achievementsComposer = achievementsComposer,
        )

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN achievements loaded successfully SHOULD emit loaded state with data`() = runTest {
        val achievementConfig = mockk<AchievementConfigDataEntity>()
        val userAchievements = listOf(mockk<AchievementEntity>())
        val commonAchievements = listOf(mockk<AchievementsUiModel>())
        val genreAchievements = listOf(mockk<AchievementsUiModel>())

        every { achievementConfigRepository.getConfig() } returns achievementConfig
        every { achievementConfig.common } returns mapOf()
        every { achievementConfig.genres } returns mapOf()
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } returns Result.success(userAchievements)
        every { achievementsComposer.compose(any(), any()) } returns commonAchievements andThen genreAchievements

        viewModel.achievementsState.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Loading),
                loading
            )

            val loaded = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Loaded, common = commonAchievements, genres = genreAchievements),
                loaded
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN achievements fetch fails SHOULD emit error state`() = runTest {
        val error = Exception("Network error")
        val achievementConfig = mockk<AchievementConfigDataEntity>()

        every { achievementConfigRepository.getConfig() } returns achievementConfig
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } returns Result.failure(error)

        viewModel.achievementsState.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Loading),
                loading
            )

            val error = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Error),
                error
            )

            cancelAndIgnoreRemainingEvents()
        }

        verify { logger.logInfo("AchievementsViewModel", any(), error) }
    }

    @Test
    fun `WHEN flow throws exception SHOULD emit error state`() = runTest {
        val error = Exception("Flow error")

        val achievementConfig = mockk<AchievementConfigDataEntity>()

        every { achievementConfigRepository.getConfig() } returns achievementConfig
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } throws error

        viewModel.achievementsState.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Loading),
                loading
            )

            val error = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Error),
                error
            )

            cancelAndIgnoreRemainingEvents()
        }
        verify { logger.logInfo("AchievementsViewModel", any(), error) }
    }

    @Test
    fun `WHEN achievements are empty SHOULD emit empty state`() = runTest {
        val achievementConfig = mockk<AchievementConfigDataEntity>()
        val userAchievements = listOf(mockk<AchievementEntity>())

        every { achievementConfigRepository.getConfig() } returns achievementConfig
        every { achievementConfig.common } returns mapOf()
        every { achievementConfig.genres } returns mapOf()
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } returns Result.success(userAchievements)
        every { achievementsComposer.compose(any(), any()) } returns emptyList()

        viewModel.achievementsState.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Loading),
                loading
            )

            val empty = awaitItem()
            assertEquals(
                AchievementsUiState(status = Status.Empty),
                empty
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val USER_ID = 213L
    }
}
