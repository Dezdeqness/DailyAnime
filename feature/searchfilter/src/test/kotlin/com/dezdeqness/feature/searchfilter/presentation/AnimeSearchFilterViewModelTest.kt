package com.dezdeqness.feature.searchfilter.presentation

import app.cash.turbine.test
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.repository.SearchFilterRepository
import com.dezdeqness.contract.filter.model.AnimeCell
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeSearchFilterViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var composer: AnimeSearchFilterComposer

    @MockK
    private lateinit var searchFilterRepository: SearchFilterRepository

    private lateinit var viewModel: AnimeSearchFilterViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = AnimeSearchFilterViewModel(
            animeSearchFilterComposer = composer,
            searchFilterRepository = searchFilterRepository,
            coroutineDispatcherProvider = object : CoroutineDispatcherProvider {
                override fun main() = Dispatchers.Main
                override fun io() = Dispatchers.Main
                override fun computation() = Dispatchers.Main
            },
            logger = logger,
        )

        every { logger.logInfo(any(), any()) } returns Unit
        every { logger.logInfo(any(), any(), any()) } returns Unit
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN filters received empty SHOULD compose from repository and show sheet`() = runTest {
        val config = listOf<FilterEntity>()
        val composed = listOf(section())

        every { searchFilterRepository.getFilterConfiguration() } returns config
        every { composer.compose(config) } returns composed

        viewModel.onFiltersReceived(listOf())

        val state = viewModel.animeSearchFilterStateFlow.value
        assertTrue(state.isFilterVisible)
        assertEquals(1, state.items.size)
        assertEquals(composed.first(), state.items.first().value)
    }

    @Test
    fun `WHEN cell clicked unselected SHOULD add to selected cells`() = runTest {
        every { searchFilterRepository.getFilterConfiguration() } returns listOf()
        every { composer.compose(any()) } returns listOf(section())

        viewModel.onFiltersReceived(listOf())

        viewModel.onCellClicked(innerId = "kind", cellId = "tv", isSelected = false)

        val state = viewModel.animeSearchFilterStateFlow.value
        assertTrue(state.items.first().value.selectedCells.contains("tv"))
        assertTrue(state.selectedCells.any { it.id == "tv" })
    }

    @Test
    fun `WHEN cell clicked selected SHOULD remove from selected cells`() = runTest {
        every { searchFilterRepository.getFilterConfiguration() } returns listOf()
        every { composer.compose(any()) } returns listOf(section())

        viewModel.onFiltersReceived(listOf())
        viewModel.onCellClicked(innerId = "kind", cellId = "tv", isSelected = false)
        viewModel.onCellClicked(innerId = "kind", cellId = "tv", isSelected = true)

        val state = viewModel.animeSearchFilterStateFlow.value
        assertFalse(state.items.first().value.selectedCells.contains("tv"))
        assertFalse(state.selectedCells.any { it.id == "tv" })
    }

    @Test
    fun `WHEN apply clicked SHOULD emit applied filters and hide sheet`() = runTest {
        every { searchFilterRepository.getFilterConfiguration() } returns listOf()
        every { composer.compose(any()) } returns listOf(section())

        viewModel.onFiltersReceived(listOf())

        viewModel.appliedFilters.test {
            viewModel.onApplyButtonClicked()

            val applied = awaitItem()
            assertEquals(1, applied.size)

            assertFalse(viewModel.animeSearchFilterStateFlow.value.isFilterVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN reset clicked SHOULD emit empty filters and clear state`() = runTest {
        every { searchFilterRepository.getFilterConfiguration() } returns listOf()
        every { composer.compose(any()) } returns listOf(section())

        viewModel.onFiltersReceived(listOf())

        viewModel.appliedFilters.test {
            viewModel.onResetButtonClicked()

            val applied = awaitItem()
            assertTrue(applied.isEmpty())

            val state = viewModel.animeSearchFilterStateFlow.value
            assertTrue(state.items.isEmpty())
            assertTrue(state.selectedCells.isEmpty())
            assertFalse(state.isFilterVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun section() = SearchSectionUiModel(
        innerId = "kind",
        displayName = "Type",
        items = listOf(
            AnimeCell(id = "tv", displayName = "TV"),
            AnimeCell(id = "movie", displayName = "Movie"),
        ),
    )
}
