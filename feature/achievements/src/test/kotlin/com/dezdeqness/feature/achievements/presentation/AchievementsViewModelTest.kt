package com.dezdeqness.feature.achievements.presentation

import app.cash.turbine.test
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.test.MainDispatcherExtension
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.domain.model.AchievementConfigDataEntity
import com.dezdeqness.domain.model.AchievementEntity
import com.dezdeqness.domain.repository.AchievementRepository
import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class AchievementsViewModelTest {

    @MockK
    private lateinit var achievementRepository: AchievementRepository

    @MockK
    private lateinit var configurationProvider: ConfigurationProvider

    @MockK
    private lateinit var appLogger: AppLogger

    @MockK
    private lateinit var achievementsComposer: AchievementsComposer

    private lateinit var viewModel: AchievementsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = AchievementsViewModel(
            userId = USER_ID,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            appLogger = appLogger,
            achievementRepository = achievementRepository,
            configurationProvider = configurationProvider,
            achievementsComposer = achievementsComposer,
        )

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit
    }

    @Test
    fun `WHEN achievements loaded successfully SHOULD emit loaded state with data`() = runTest {
        val achievementConfig = mockk<AchievementConfigDataEntity>()
        val userAchievements = listOf(mockk<AchievementEntity>())
        val commonAchievements = listOf(mockk<AchievementsUiModel>())
        val genreAchievements = listOf(mockk<AchievementsUiModel>())

        every { configurationProvider.getAchievementConfig() } returns achievementConfig
        every { achievementConfig.common } returns mapOf()
        every { achievementConfig.genres } returns mapOf()
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } returns Result.success(userAchievements)
        every { achievementsComposer.compose(any(), any()) } returns commonAchievements andThen genreAchievements

        viewModel.achievementsState.test {
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

        every { configurationProvider.getAchievementConfig() } returns achievementConfig
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } returns Result.failure(error)

        viewModel.achievementsState.test {
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

        verify { appLogger.logInfo("AchievementsViewModel", throwable = error) }
    }

    @Test
    fun `WHEN flow throws exception SHOULD emit error state`() = runTest {
        val error = Exception("Flow error")

        val achievementConfig = mockk<AchievementConfigDataEntity>()

        every { configurationProvider.getAchievementConfig() } returns achievementConfig
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } throws error

        viewModel.achievementsState.test {
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
        verify { appLogger.logInfo("AchievementsViewModel", throwable = error) }
    }

    @Test
    fun `WHEN achievements are empty SHOULD emit empty state`() = runTest {
        val achievementConfig = mockk<AchievementConfigDataEntity>()
        val userAchievements = listOf(mockk<AchievementEntity>())

        every { configurationProvider.getAchievementConfig() } returns achievementConfig
        every { achievementConfig.common } returns mapOf()
        every { achievementConfig.genres } returns mapOf()
        coEvery { achievementRepository.fetchAchievementsByUserId(USER_ID) } returns Result.success(userAchievements)
        every { achievementsComposer.compose(any(), any()) } returns emptyList()

        viewModel.achievementsState.test {
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
