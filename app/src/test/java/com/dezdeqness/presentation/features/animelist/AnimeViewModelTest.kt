package com.dezdeqness.presentation.features.animelist

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.repository.HistorySearchRepository
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
import com.dezdeqness.presentation.AnimeFilterResponseConverter
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.message.MessageConsumer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import utils.TestCoroutineDispatcherProvider


class AnimeViewModelTest {

    @MockK
    private lateinit var getAnimeListUseCase: GetAnimeListUseCase

    @MockK
    private lateinit var animeUiMapper: AnimeUiMapper

    @MockK
    private lateinit var animeFilterResponseConverter: AnimeFilterResponseConverter

    @MockK
    private lateinit var actionConsumer: ActionConsumer

    @MockK
    private lateinit var messageConsumer: MessageConsumer

    @MockK
    private lateinit var messageProvider: MessageProvider

    @MockK
    private lateinit var appLogger: AppLogger

    @MockK
    private lateinit var historySearchRepository: HistorySearchRepository

    private lateinit var viewModel: AnimeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { actionConsumer.attachListener(any()) } returns Unit

        every { historySearchRepository.getSearchHistoryFlow() } returns flowOf(listOf())

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit

        viewModel = AnimeViewModel(
            getAnimeListUseCase = getAnimeListUseCase,
            animeUiMapper = animeUiMapper,
            animeFilterResponseConverter = animeFilterResponseConverter,
            actionConsumer = actionConsumer,
            messageConsumer = messageConsumer,
            messageProvider = messageProvider,
            historySearchRepository = historySearchRepository,
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            appLogger = appLogger,
        )
    }

    @Test
    fun `WHEN user open page AND initial load IS success SHOULD show first page`() = runBlocking {
        val entityItem = mockk<AnimeBriefEntity>()
        val successListEntity = listOf(entityItem)
        val uiItem = mockk<AnimeUiModel>()
        val exceptedUiList = listOf(uiItem)

        every {
            animeFilterResponseConverter.convertSearchFilterToQueryMap(listOf())
        } returns mapOf()

        every {
            getAnimeListUseCase.invoke(
                pageNumber = 1,
                queryMap = mapOf(),
                searchQuery = ""
            )
        } returns Result.success(
            GetAnimeListUseCase.AnimeListState(
                list = successListEntity,
                currentPage = 1,
                hasNextPage = true,
            )
        )

        every { animeUiMapper.map(successListEntity) } returns exceptedUiList

        viewModel.animeSearchState.test {
            val initial = awaitItem()
            assertEquals(AnimeSearchState(), initial)

            val loaded = awaitItem()
            val expected = AnimeSearchState(
                list = exceptedUiList,
                status = AnimeSearchStatus.Loaded,
                currentPage = 1,
                hasNextPage = true,
                input = AnimeUserInput()
            )

            assertEquals(expected, loaded)
        }
    }


    @Test
    fun `WHEN user open page AND initial load IS failure SHOULD show error state`() = runBlocking {
        val entityItem = mockk<AnimeBriefEntity>()
        val successListEntity = listOf(entityItem)
        val uiItem = mockk<AnimeUiModel>()
        val exceptedUiList = listOf(uiItem)

        every {
            animeFilterResponseConverter.convertSearchFilterToQueryMap(listOf())
        } returns mapOf()

        every {
            getAnimeListUseCase.invoke(
                pageNumber = 1,
                queryMap = mapOf(),
                searchQuery = ""
            )
        } returns Result.failure(Exception())

        every { animeUiMapper.map(successListEntity) } returns exceptedUiList

        viewModel.animeSearchState.test {
            val initial = awaitItem()
            assertEquals(AnimeSearchState(), initial)

            val loaded = awaitItem()
            val expected = AnimeSearchState(
                list = listOf(),
                status = AnimeSearchStatus.Error,
                currentPage = 1,
                hasNextPage = false,
                input = AnimeUserInput()
            )

            assertEquals(expected, loaded)
        }
    }

}
