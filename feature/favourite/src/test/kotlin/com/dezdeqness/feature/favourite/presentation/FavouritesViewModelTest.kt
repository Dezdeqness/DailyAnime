package com.dezdeqness.feature.favourite.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.test.MainDispatcherExtension
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.feature.favourite.presentation.models.FavouritesUiModel
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
class FavouritesViewModelTest {

    @MockK
    private lateinit var favouriteRepository: FavouriteRepository

    @MockK
    private lateinit var favouriteMapper: FavouriteMapper

    @MockK
    private lateinit var appLogger: AppLogger

    private lateinit var viewModel: FavouritesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = FavouritesViewModel(
            userId = USER_ID,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            appLogger = appLogger,
            favouriteRepository = favouriteRepository,
            favouriteMapper = favouriteMapper,
        )

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit
    }

    @Test
    fun `WHEN favourites loaded successfully SHOULD emit loaded state with data`() = runTest {
        val favourites = listOf(mockk<FavouriteEntity>())
        val uiItem = mockk<FavouritesUiModel>()

        coEvery { favouriteRepository.getFavourites(USER_ID) } returns Result.success(favourites)
        every { favouriteMapper.map(any()) } returns uiItem

        viewModel.favouritesState.test {
            val initial = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Loading),
                loading
            )

            val loaded = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Loaded, items = listOf(uiItem)),
                loaded
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN favourites are empty SHOULD emit empty state`() = runTest {
        coEvery { favouriteRepository.getFavourites(USER_ID) } returns Result.success(listOf())

        viewModel.favouritesState.test {
            val initial = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Loading),
                loading
            )

            val empty = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Empty),
                empty
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN favourites fetch fails SHOULD emit error state`() = runTest {
        val error = Exception("Network error")

        coEvery { favouriteRepository.getFavourites(USER_ID) } returns Result.failure(error)

        viewModel.favouritesState.test {
            val initial = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Loading),
                loading
            )

            val error = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Error),
                error
            )

            cancelAndIgnoreRemainingEvents()
        }

        verify { appLogger.logInfo("FavouritesViewModel", throwable = error) }
    }

    @Test
    fun `WHEN flow throws exception SHOULD emit error state`() = runTest {
        val error = Exception("Flow error")

        coEvery { favouriteRepository.getFavourites(USER_ID) } throws error

        viewModel.favouritesState.test {
            val initial = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Initial),
                initial
            )

            val loading = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Loading),
                loading
            )

            val error = awaitItem()
            assertEquals(
                FavouritesUiState(status = Status.Error),
                error
            )

            cancelAndIgnoreRemainingEvents()
        }
        verify { appLogger.logInfo("FavouritesViewModel", throwable = error) }
    }

    companion object {
        private const val USER_ID = 456L
    }
}
