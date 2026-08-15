package com.dezdeqness.feature.home.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.utils.ImageUrlUtils
import com.dezdeqness.contract.home.model.HomeEntity
import com.dezdeqness.contract.home.repository.HomeRepository
import com.dezdeqness.domain.usecases.GetLatestHistoryItemUseCase
import com.dezdeqness.feature.home.presentation.models.SectionAnimeUiModel
import com.dezdeqness.feature.home.presentation.models.SectionStatus
import com.dezdeqness.feature.home.presentation.models.SectionUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var homeRepository: HomeRepository

    @MockK
    private lateinit var homeUiMapper: HomeUiMapper

    @MockK
    private lateinit var homeGenresProvider: HomeGenresProvider

    @MockK
    private lateinit var sessionManager: SessionManager

    @MockK
    private lateinit var configManager: ConfigManager

    @MockK
    private lateinit var getLatestHistoryItemUseCase: GetLatestHistoryItemUseCase

    @MockK
    private lateinit var imageUrlUtils: ImageUrlUtils

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    @MockK
    private lateinit var homeComposer: HomeComposer

    private val sessionStateFlow = MutableStateFlow<SessionState>(SessionState.Unauthenticated)

    private val initialSections = SectionsState(
        genreSections = listOf(
            SectionUiModel(id = "shounen", numericId = GENRE_ID, title = "Shounen"),
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
        every { sessionManager.sessionState } returns sessionStateFlow
        every {
            settingsRepository.observePreference(UserSelectedInterestsPreference)
        } returns emptyFlow()
        coEvery { homeComposer.composeSectionsInitial() } returns initialSections
        coEvery { homeGenresProvider.getHomeSectionGenresIds() } returns listOf(GENRE_ID)
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = HomeViewModel(
        homeRepository = homeRepository,
        homeUiMapper = homeUiMapper,
        homeGenresProvider = homeGenresProvider,
        sessionManager = sessionManager,
        configManager = configManager,
        getLatestHistoryItemUseCase = getLatestHistoryItemUseCase,
        imageUrlUtils = imageUrlUtils,
        settingsRepository = settingsRepository,
        homeComposer = homeComposer,
        coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
            override fun main() = Dispatchers.Main
            override fun io() = Dispatchers.Main
            override fun computation() = Dispatchers.Main
        },
        logger = logger,
    )

    @Test
    fun `WHEN view model created SHOULD compose initial sections skeleton`() = runTest {
        val viewModel = createViewModel()

        viewModel.homeStateFlow.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(initialSections.genreSections, state.sectionsState.genreSections)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN sections loaded successfully SHOULD emit loaded sections`() = runTest {
        val animeEntity = mockk<AnimeBriefEntity>()
        val sectionItem = SectionAnimeUiModel(id = 1L, title = "Fullmetal Alchemist", logoUrl = "")

        coEvery { homeRepository.getHomeSections(listOf(GENRE_ID)) } returns Result.success(
            HomeEntity(
                calendarSection = emptyList(),
                genreSections = mapOf(GENRE_ID to listOf(animeEntity)),
            ),
        )
        every { homeUiMapper.mapSectionAnimeModel(animeEntity) } returns sectionItem
        every { configManager.isCalendarEnabled } returns true
        every { getLatestHistoryItemUseCase.invoke() } returns Result.success(null)

        val viewModel = createViewModel()

        viewModel.homeStateFlow.test {
            advanceUntilIdle()

            viewModel.onInitialLoad()
            advanceUntilIdle()

            val state = expectMostRecentItem()

            val section = state.sectionsState.genreSections.single()
            assertEquals(SectionStatus.Loaded, section.status)
            assertEquals(listOf(sectionItem), section.items)

            assertEquals(SectionStatus.Loaded, state.sectionsState.calendarSection.status)
            assertEquals(SectionStatus.Loaded, state.sectionsState.latestHistoryItem.status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN sections loading fails SHOULD emit error statuses`() = runTest {
        val error = Exception("Network error")

        coEvery { homeRepository.getHomeSections(listOf(GENRE_ID)) } returns Result.failure(error)
        every { getLatestHistoryItemUseCase.invoke() } returns Result.failure(error)

        val viewModel = createViewModel()

        viewModel.homeStateFlow.test {
            advanceUntilIdle()

            viewModel.onInitialLoad()
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertTrue(state.sectionsState.genreSections.all { it.status == SectionStatus.Error })
            assertEquals(SectionStatus.Error, state.sectionsState.calendarSection.status)
            assertEquals(SectionStatus.Error, state.sectionsState.latestHistoryItem.status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN session is authenticated SHOULD emit authorized state`() = runTest {
        sessionStateFlow.value = SessionState.Authenticated(
            userId = 1L,
            nickname = "Astaroth",
            avatar = "avatarUrl",
        )

        val viewModel = createViewModel()

        viewModel.homeStateFlow.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(
                AuthorizedState(
                    isAuthorized = true,
                    userName = "Astaroth",
                    avatarUrl = "avatarUrl",
                ),
                state.authorizedState,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val GENRE_ID = "27"
    }
}
