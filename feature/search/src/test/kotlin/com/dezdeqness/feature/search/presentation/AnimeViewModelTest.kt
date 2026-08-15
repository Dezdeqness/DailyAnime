package com.dezdeqness.feature.search.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.history.repository.HistorySearchRepository
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var getAnimeListUseCase: GetAnimeListUseCase

    @MockK
    private lateinit var animeUiMapper: AnimeUiMapper

    @MockK
    private lateinit var animeFilterResponseConverter: AnimeFilterResponseConverter

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: BaseMessageProvider

    @MockK
    private lateinit var historySearchRepository: HistorySearchRepository

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var viewModel: AnimeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
        every {
            settingsRepository.observePreference(AdultContentPreference)
        } returns emptyFlow()
        every { historySearchRepository.getSearchHistoryFlow() } returns flowOf(listOf())
        every { animeFilterResponseConverter.convertSearchFilterToQueryMap(any()) } returns mapOf()

        viewModel = AnimeViewModel(
            getAnimeListUseCase = getAnimeListUseCase,
            animeUiMapper = animeUiMapper,
            animeFilterResponseConverter = animeFilterResponseConverter,
            messageConsumer = messageConsumer,
            messageProvider = messageProvider,
            historySearchRepository = historySearchRepository,
            settingsRepository = settingsRepository,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
        )
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN list loaded successfully SHOULD emit loaded state`() = runTest {
        val entity = mockk<AnimeBriefEntity>()
        val uiItems = listOf(animeItem())

        coEvery { getAnimeListUseCase.invoke(any(), any(), any()) } returns Result.success(
            GetAnimeListUseCase.AnimeListState(
                list = listOf(entity),
                hasNextPage = true,
                currentPage = 2,
            ),
        )
        every { animeUiMapper.map(listOf(entity)) } returns uiItems

        viewModel.animeSearchState.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(uiItems, state.list)
            assertEquals(AnimeSearchStatus.Loaded, state.status)
            assertEquals(true, state.hasNextPage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN list empty SHOULD emit empty state`() = runTest {
        coEvery { getAnimeListUseCase.invoke(any(), any(), any()) } returns Result.success(
            GetAnimeListUseCase.AnimeListState(list = listOf(), hasNextPage = false, currentPage = 1),
        )
        every { animeUiMapper.map(listOf()) } returns listOf()

        viewModel.animeSearchState.test {
            advanceUntilIdle()

            assertEquals(AnimeSearchStatus.Empty, expectMostRecentItem().status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN initial load fails SHOULD emit error state`() = runTest {
        coEvery { getAnimeListUseCase.invoke(any(), any(), any()) } returns Result.failure(Exception("boom"))

        viewModel.animeSearchState.test {
            advanceUntilIdle()

            assertEquals(AnimeSearchStatus.Error, expectMostRecentItem().status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN query changed SHOULD reload and store search history`() = runTest {
        val entity = mockk<AnimeBriefEntity>()
        coEvery { getAnimeListUseCase.invoke(any(), any(), any()) } returns Result.success(
            GetAnimeListUseCase.AnimeListState(list = listOf(entity), hasNextPage = false, currentPage = 1),
        )
        every { animeUiMapper.map(any()) } returns listOf(animeItem())
        coEvery { historySearchRepository.addSearchHistory(any()) } returns Unit

        viewModel.animeSearchState.test {
            advanceUntilIdle()

            viewModel.onQueryChanged("naruto")
            advanceUntilIdle()

            assertEquals("naruto", expectMostRecentItem().input.query)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { historySearchRepository.addSearchHistory("naruto") }
    }

    @Test
    fun `WHEN load more SHOULD append next page`() = runTest {
        val entity = mockk<AnimeBriefEntity>()
        coEvery { getAnimeListUseCase.invoke(pageNumber = 1, any(), any()) } returns Result.success(
            GetAnimeListUseCase.AnimeListState(list = listOf(entity), hasNextPage = true, currentPage = 2),
        )
        coEvery { getAnimeListUseCase.invoke(pageNumber = 2, any(), any()) } returns Result.success(
            GetAnimeListUseCase.AnimeListState(list = listOf(entity), hasNextPage = false, currentPage = 3),
        )
        every { animeUiMapper.map(any()) } returns listOf(animeItem()) andThen listOf(animeItem(id = 2L))

        viewModel.animeSearchState.test {
            advanceUntilIdle()

            viewModel.onLoadMore()
            advanceUntilIdle()

            val state = expectMostRecentItem()
            assertEquals(2, state.list.size)
            assertEquals(AnimeSearchStatus.Loaded, state.status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun animeItem(id: Long = 1L) =
        AnimeUiModel(id = id, title = "Naruto", kind = "TV", logoUrl = "")
}
