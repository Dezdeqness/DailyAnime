package com.dezdeqness.presentation.features.home

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.domain.model.HomeCalendarEntity
import com.dezdeqness.domain.model.HomeEntity
import com.dezdeqness.domain.model.ImageEntity
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.domain.repository.HomeRepository
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.features.home.model.HomeCalendarUiModel
import com.dezdeqness.presentation.features.home.model.SectionAnimeUiModel
import com.dezdeqness.presentation.features.home.model.SectionStatus
import com.dezdeqness.presentation.features.home.model.SectionUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertAll
import utils.TestCoroutineDispatcherProvider

class HomeViewModelTest {

    @MockK
    private lateinit var homeRepository: HomeRepository

    @MockK
    private lateinit var actionConsumer: ActionConsumer

    @MockK
    private lateinit var animeUiMapper: AnimeUiMapper

    @MockK
    private lateinit var homeGenresProvider: HomeGenresProvider

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var appLogger: AppLogger

    @MockK
    private lateinit var configManager: ConfigManager

    @MockK
    private lateinit var homeComposer: HomeComposer

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { actionConsumer.attachListener(any()) } returns Unit

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit

        every { configManager.isCalendarEnabled } returns true

        every { authRepository.authorizationState() } returns MutableSharedFlow()

        every { homeComposer.composeSectionsInitial() } returns SectionsState(genreSections = DEFAULT_SECTIONS)
        every { homeGenresProvider.getHomeSectionGenresIds() } returns DEFAULT_SECTIONS_IDS
        every { authRepository.isAuthorized() } returns true

        val accountEntity = mockk<AccountEntity>()
        every { userRepository.getProfileLocal() } returns accountEntity
        every { accountEntity.nickname } returns DEFAULT_USERNAME
        every { accountEntity.avatar } returns DEFAULT_AVATAR_URL

        viewModel = HomeViewModel(
            animeUiMapper = animeUiMapper,
            actionConsumer = actionConsumer,
            homeGenresProvider = homeGenresProvider,
            userRepository = userRepository,
            homeRepository = homeRepository,
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            appLogger = appLogger,
            homeComposer = homeComposer,
            configManager = configManager,
            authRepository = authRepository,
        )
    }

    @Test
    fun `WHEN home IS load success SHOULD show home sections`() = runBlocking {
        val homeEntity = mockk<HomeEntity>()
        val sectionAnimeUiModel = mockk<SectionAnimeUiModel>()
        val sectionCalendarUiModel = mockk<HomeCalendarUiModel>()
        coEvery {
            homeRepository.getHomeSections(DEFAULT_SECTIONS_IDS)
        } returns Result.success(homeEntity)
        every { animeUiMapper.mapSectionAnimeModel(any()) } returns sectionAnimeUiModel
        every { animeUiMapper.mapHomeCalendarAnimeModel(any()) } returns sectionCalendarUiModel
        every { homeEntity.genreSections } returns DEFAULT_SECTIONS_MAP
        every { homeEntity.calendarSection } returns DEFAULT_CALENDAR_ENTITY_LIST

        viewModel.onInitialLoad()

        val uiState = viewModel.homeStateFlow.value

        assertAll(
            { assertTrue(uiState.sectionsState.genreSections.any { it.status == SectionStatus.Loaded }) },
            { assertTrue(uiState.sectionsState.calendarSection.status == SectionStatus.Loaded) },
        )

    }

    @Test
    fun `WHEN home IS load failure SHOULD show home sections with error state`() = runBlocking {
        coEvery {
            homeRepository.getHomeSections(DEFAULT_SECTIONS_IDS)
        } returns Result.failure(Throwable())

        viewModel.onInitialLoad()

        val uiState = viewModel.homeStateFlow.value

        assertAll(
            { assertTrue(uiState.sectionsState.genreSections.any { it.status == SectionStatus.Error }) },
        )

    }

    companion object {
        private const val DEFAULT_USERNAME = "Astra"
        private const val DEFAULT_AVATAR_URL = "url"
        private val DEFAULT_SECTIONS = listOf(
            SectionUiModel(
                id = "id1",
                numericId = "numericId1",
                title = "title1",
            ),
            SectionUiModel(
                id = "id2",
                numericId = "numericId2",
                title = "title1",
            ),
            SectionUiModel(
                id = "id3",
                numericId = "numericId3",
                title = "title1",
            )
        )

        private val DEFAULT_SECTIONS_ENTITY_LIST = listOf(
            AnimeBriefEntity(
                id = 0,
                name = "",
                airedOnTimestamp = 0L,
                episodes = 0,
                episodesAired = 0,
                image = ImageEntity(),
                kind = AnimeKind.TV,
                releasedOnTimestamp = 0L,
                russian = "",
                score = 0f,
                status = AnimeStatus.LATEST,
                url = "",
            )
        )

        private val DEFAULT_CALENDAR_ENTITY_LIST = listOf(
            HomeCalendarEntity(
                id = 0,
                name = "",
                airedOnTimestamp = 0L,
                episodes = 0,
                episodesAired = 0,
                image = ImageEntity(),
                kind = AnimeKind.TV,
                releasedOnTimestamp = 0L,
                russian = "",
                score = 0f,
                status = AnimeStatus.LATEST,
                url = "",
                description = "",
                nextEpisodeTimestamp = 100,
            )
        )

        private val DEFAULT_SECTIONS_MAP = mapOf(
            "numericId1" to DEFAULT_SECTIONS_ENTITY_LIST,
            "numericId2" to DEFAULT_SECTIONS_ENTITY_LIST,
            "numericId3" to DEFAULT_SECTIONS_ENTITY_LIST,
        )

        private val DEFAULT_SECTIONS_IDS = DEFAULT_SECTIONS.map { it.numericId }
    }

}
