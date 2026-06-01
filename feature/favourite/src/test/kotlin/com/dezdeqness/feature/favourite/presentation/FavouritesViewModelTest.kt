package com.dezdeqness.feature.favourite.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouritesCacheState
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.Logger
import com.dezdeqness.feature.favourite.presentation.models.FavouritesUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesViewModelTest {

    @MockK
    private lateinit var favouriteRepository: FavouriteRepository

    @MockK
    private lateinit var favouriteMapper: FavouriteMapper

    @MockK
    private lateinit var logger: Logger

    private lateinit var favouritesFlow: MutableStateFlow<FavouritesCacheState>
    private lateinit var viewModel: FavouritesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        favouritesFlow = MutableStateFlow(FavouritesCacheState.Empty)
        every { favouriteRepository.favourites } returns favouritesFlow
        coEvery { favouriteRepository.fetchFavourites(any(), any()) } returns Result.success(Unit)

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit

        viewModel = FavouritesViewModel(
            userId = USER_ID,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
            favouriteRepository = favouriteRepository,
            favouriteMapper = favouriteMapper,
        )
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN favourites loaded with items SHOULD emit loaded state`() = runTest {
        val favourite = mockk<FavouriteEntity>()
        val uiItem = mockk<FavouritesUiModel>()
        every { favouriteMapper.map(favourite) } returns uiItem

        viewModel.favouritesState.test {
            advanceUntilIdle()

            assertEquals(FavouritesUiState(status = Status.Initial), awaitItem())

            favouritesFlow.value = FavouritesCacheState.Loading
            advanceUntilIdle()
            assertEquals(FavouritesUiState(status = Status.Loading), awaitItem())

            favouritesFlow.value = FavouritesCacheState.Loaded(
                items = listOf(favourite),
                loadedAtMillis = 0L,
            )
            advanceUntilIdle()
            assertEquals(
                FavouritesUiState(status = Status.Loaded, items = listOf(uiItem)),
                awaitItem(),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN favourites are empty SHOULD emit empty state`() = runTest {
        viewModel.favouritesState.test {
            advanceUntilIdle()

            assertEquals(FavouritesUiState(status = Status.Initial), awaitItem())

            favouritesFlow.value = FavouritesCacheState.Loaded(items = emptyList(), loadedAtMillis = 0L)
            advanceUntilIdle()
            assertEquals(FavouritesUiState(status = Status.Empty), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN favourites fetch fails SHOULD emit error state`() = runTest {
        val error = Exception("Network error")

        viewModel.favouritesState.test {
            advanceUntilIdle()

            assertEquals(FavouritesUiState(status = Status.Initial), awaitItem())

            favouritesFlow.value = FavouritesCacheState.Error(error)
            advanceUntilIdle()
            assertEquals(FavouritesUiState(status = Status.Error), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val USER_ID = 456L
    }
}
