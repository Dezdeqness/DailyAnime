package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.usecases.GetAnimeListUseCase
import com.dezdeqness.presentation.AnimeFilterResponseConverter
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.action.ActionConsumer
import com.dezdeqness.presentation.message.MessageConsumer
import com.dezdeqness.presentation.models.AnimeUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertAll
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

    private lateinit var viewModel: AnimeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { actionConsumer.attachListener(any()) } returns Unit

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit

        viewModel = AnimeViewModel(
            getAnimeListUseCase = getAnimeListUseCase,
            animeUiMapper = animeUiMapper,
            animeFilterResponseConverter = animeFilterResponseConverter,
            actionConsumer = actionConsumer,
            messageConsumer = messageConsumer,
            messageProvider = messageProvider,
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
        } returns Result.success(GetAnimeListUseCase.AnimeListState(list = successListEntity))
        every {
            animeUiMapper.map(successListEntity)
        } returns exceptedUiList

        viewModel.onInitialLoad()

        val uiState = viewModel.animeSearchStateFlow.value

        assertAll(
            { assertFalse(uiState.isLoadingStateShowing) },
            { assertEquals(exceptedUiList, uiState.list) },
        )
    }

    @Test
    fun `WHEN user open page AND initial load IS failure SHOULD show show error state`() =
        runBlocking {
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

            viewModel.onInitialLoad()

            val uiState = viewModel.animeSearchStateFlow.value

            assertAll(
                { assertFalse(uiState.isLoadingStateShowing) },
                { assertTrue(uiState.isErrorStateShowing) },
            )
        }

}
