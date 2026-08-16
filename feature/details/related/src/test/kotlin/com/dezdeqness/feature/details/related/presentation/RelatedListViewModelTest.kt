package com.dezdeqness.feature.details.related.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.Entity
import com.dezdeqness.domain.anime.usecases.BaseListableUseCase
import com.dezdeqness.feature.details.related.presentation.models.ChronologyUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
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
class RelatedListViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var mapper: RelatedListUiMapper

    @MockK
    private lateinit var baseListableUseCase: BaseListableUseCase

    private lateinit var viewModel: RelatedListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = RelatedListViewModel(
            animeId = ANIME_ID,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
            mapper = mapper,
            baseListableUseCase = baseListableUseCase,
        )

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN list loaded successfully SHOULD emit loaded state with items`() = runTest {
        val entity = mockk<Entity>()
        val item = chronologyItem()

        coEvery { baseListableUseCase.invoke(ANIME_ID) } returns Result.success(listOf(entity))
        every { mapper.map(entity) } returns item

        viewModel.stateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Initial), initial)

            val loading = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Loading), loading)

            val loaded = awaitItem()
            assertEquals(
                RelatedListState(list = listOf(item), status = RelatedListStatus.Loaded),
                loaded,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN loaded list is empty SHOULD emit empty state`() = runTest {
        coEvery { baseListableUseCase.invoke(ANIME_ID) } returns Result.success(emptyList())

        viewModel.stateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Initial), initial)

            val loading = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Loading), loading)

            val empty = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Empty), empty)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN loading fails SHOULD emit error state`() = runTest {
        val error = Exception("Network error")

        coEvery { baseListableUseCase.invoke(ANIME_ID) } returns Result.failure(error)

        viewModel.stateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Initial), initial)

            val loading = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Loading), loading)

            val errorState = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Error), errorState)

            cancelAndIgnoreRemainingEvents()
        }

        verify { logger.logInfo("RelatedListViewModel", any(), error) }
    }

    @Test
    fun `WHEN use case throws exception SHOULD emit error state`() = runTest {
        val error = Exception("Flow error")

        coEvery { baseListableUseCase.invoke(ANIME_ID) } throws error

        viewModel.stateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Initial), initial)

            val loading = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Loading), loading)

            val errorState = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Error), errorState)

            cancelAndIgnoreRemainingEvents()
        }

        verify { logger.logInfo("RelatedListViewModel", any(), error) }
    }

    @Test
    fun `WHEN retry clicked after error SHOULD reload and emit loaded state`() = runTest {
        val entity = mockk<Entity>()
        val item = chronologyItem()

        coEvery { baseListableUseCase.invoke(ANIME_ID) } returns Result.failure(Exception("Network error"))

        viewModel.stateFlow.test {
            advanceUntilIdle()

            val initial = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Initial), initial)

            val loading = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Loading), loading)

            val errorState = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Error), errorState)

            coEvery { baseListableUseCase.invoke(ANIME_ID) } returns Result.success(listOf(entity))
            every { mapper.map(entity) } returns item

            viewModel.onRetryClicked()
            advanceUntilIdle()

            val retryLoading = awaitItem()
            assertEquals(RelatedListState(status = RelatedListStatus.Loading), retryLoading)

            val loaded = awaitItem()
            assertEquals(
                RelatedListState(list = listOf(item), status = RelatedListStatus.Loaded),
                loaded,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun chronologyItem() = ChronologyUiModel(
        id = 1L,
        name = "Fullmetal Alchemist: Brotherhood",
        imageUrl = "url",
        briefInfo = "TV • 2009",
    )

    companion object {
        private const val ANIME_ID = 42L
    }
}
