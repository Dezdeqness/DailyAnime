package com.dezdeqness.feature.onboarding.selectgenres.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.settings.models.UserSelectedInterestsPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.HomeGenresProvider
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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SelectGenresViewModelTest {

    @MockK
    private lateinit var configurationProvider: ConfigurationProvider

    @MockK
    private lateinit var mapper: SelectGenresMapper

    @MockK
    private lateinit var appLogger: AppLogger

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    @MockK
    private lateinit var homeGenresProvider: HomeGenresProvider

    private lateinit var viewModel: SelectGenresViewModel

    private lateinit var genres: List<GenreEntity>
    private lateinit var uiGenres: List<GenreUiModel>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SelectGenresViewModel(
            configurationProvider = configurationProvider,
            mapper = mapper,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Unconfined
                override fun io() = Dispatchers.Unconfined
                override fun computation() = Dispatchers.Unconfined
            },
            appLogger = appLogger,
            settingsRepository = settingsRepository,
            homeGenresProvider = homeGenresProvider,
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
        coEvery { homeGenresProvider.getHomeSectionGenresIds() } returns emptyList()
        coEvery {
            settingsRepository.setPreference(
                UserSelectedInterestsPreference,
                any()
            )
        } returns Unit

        every { appLogger.logInfo(any(), any()) } returns Unit
        every { appLogger.logInfo(any(), any(), any()) } returns Unit
    }

    @Test
    fun `WHEN genre clicked and not selected SHOULD add to selection`() = runTest {
        val genreId = "genre1"

        viewModel.uiState.test {
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
            coEvery { homeGenresProvider.getHomeSectionGenresIds() } returns genreIds

            viewModel.uiState.test {
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
            assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
            assertEquals(SelectGenresUiState(genres = uiGenres), awaitItem())
        }

        viewModel.events.test {
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
            assertEquals(SelectGenresUiState(genres = listOf()), awaitItem())
            ensureAllEventsConsumed()
        }

        verify { appLogger.logInfo(tag = "SelectGenresViewModel", "flow error", throwable = error) }
    }
}
