package com.dezdeqness.feature.onboarding.selectgenres.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.contract.settings.repository.UserInterestsProvider
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.Logger
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.feature.onboarding.selectgenres.presentation.models.GenreUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
class SelectGenresViewModelTest {

    @MockK
    private lateinit var configurationProvider: ConfigurationProvider

    @MockK
    private lateinit var mapper: SelectGenresMapper

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    @MockK
    private lateinit var userInterestsProvider: UserInterestsProvider

    private lateinit var viewModel: SelectGenresViewModel

    private lateinit var genres: List<GenreEntity>
    private lateinit var uiGenres: List<GenreUiModel>

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = SelectGenresViewModel(
            configurationProvider = configurationProvider,
            mapper = mapper,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
            settingsRepository = settingsRepository,
            userInterestsProvider = userInterestsProvider,
        )

        val genre1 = mockk<GenreEntity>()
        val genre2 = mockk<GenreEntity>()
        val genre3 = mockk<GenreEntity>()
        val genre4 = mockk<GenreEntity>()

        genres = listOf(genre1, genre2, genre3, genre4)
        uiGenres = listOf(
            GenreUiModel(id = "genre1", name = "Action", isGenre = true),
            GenreUiModel(id = "genre2", name = "Comedy", isGenre = true),
            GenreUiModel(id = "genre3", name = "Drama", isGenre = true),
            GenreUiModel(id = "genre4", name = "Music", isGenre = true)
        )

        every { configurationProvider.getListGenre() } returns genres
        every { mapper.map(genre1) } returns uiGenres[0]
        every { mapper.map(genre2) } returns uiGenres[1]
        every { mapper.map(genre3) } returns uiGenres[2]
        every { mapper.map(genre4) } returns uiGenres[3]
        coEvery { userInterestsProvider.getInterestIds() } returns emptyList()
        coEvery {
            settingsRepository.setPreference(
                UserSelectedInterestsPreference,
                any()
            )
        } returns Unit

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN genre clicked and not selected SHOULD add to selection`() = runTest {
        val genreId = "genre1"

        viewModel.uiState.test {
            advanceUntilIdle()

            assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
            assertEquals(SelectGenresUiState(genres = uiGenres), awaitItem())

            viewModel.onGenreClick(genreId)

            assertEquals(
                SelectGenresUiState(genres = uiGenres, selectedGenres = setOf(genreId)),
                awaitItem()
            )

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `WHEN genre clicked and already selected SHOULD remove from selection`() = runTest {
        val genreId = "genre1"

        viewModel.uiState.test {
            advanceUntilIdle()

            assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
            assertEquals(SelectGenresUiState(genres = uiGenres), awaitItem())

            viewModel.onGenreClick(genreId)

            assertEquals(
                SelectGenresUiState(genres = uiGenres, selectedGenres = setOf(genreId)),
                awaitItem()
            )

            viewModel.onGenreClick(genreId)

            assertEquals(SelectGenresUiState(genres = uiGenres), awaitItem())

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `WHEN 3 genres already selected and new genre clicked SHOULD not add new genre`() =
        runTest {
            val genreIds = listOf("genre1", "genre2", "genre3")
            coEvery { userInterestsProvider.getInterestIds() } returns genreIds

            viewModel.uiState.test {
                advanceUntilIdle()

                assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
                assertEquals(SelectGenresUiState(genres = uiGenres), awaitItem())
                assertEquals(SelectGenresUiState(genres = uiGenres, selectedGenres = genreIds.toSet()), awaitItem())

                viewModel.onGenreClick("genre4")

                assertEquals(
                    genreIds.toSet(),
                    viewModel.uiState.value.selectedGenres,
                )

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `WHEN save clicked SHOULD save selected genres to settings`() = runTest {
        coEvery {
            settingsRepository.setPreference(
                UserSelectedInterestsPreference,
                listOf("genre1", "genre2"),
            )
        } returns Unit

        viewModel.uiState.test {
            advanceUntilIdle()

            assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
            assertEquals(SelectGenresUiState(genres = uiGenres), awaitItem())
        }

        viewModel.events.test {
            advanceUntilIdle()

            viewModel.onGenreClick("genre1")
            viewModel.onGenreClick("genre2")
            viewModel.onSaveClick()
            assertEquals(SelectGenresEvent.Close, awaitItem())
            ensureAllEventsConsumed()
        }

        coVerify {
            settingsRepository.setPreference(
                UserSelectedInterestsPreference,
                listOf("genre1", "genre2")
            )
        }
    }

    @Test
    fun `WHEN flow throws exception SHOULD log error and emit empty state`() = runTest {
        val error = Exception("Configuration error")
        every { configurationProvider.getListGenre() } throws error

        viewModel.uiState.test {
            advanceUntilIdle()

            assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
            ensureAllEventsConsumed()
        }

        verify { logger.logInfo("SelectGenresViewModel", any(), error) }
    }
}
