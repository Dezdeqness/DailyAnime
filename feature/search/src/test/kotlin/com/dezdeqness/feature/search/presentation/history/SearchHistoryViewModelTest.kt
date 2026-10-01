package com.dezdeqness.feature.search.presentation.history

import app.cash.turbine.test
import com.dezdeqness.contract.history.repository.HistorySearchRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SearchHistoryViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val historySearchRepository = mockk<HistorySearchRepository>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { historySearchRepository.getSearchHistoryFlow() } returns flowOf(listOf("naruto", "bleach"))
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN observed SHOULD expose stored history`() = runTest(dispatcher) {
        val viewModel = SearchHistoryViewModel(historySearchRepository)

        viewModel.history.test {
            assertEquals(listOf<String>(), awaitItem())
            assertEquals(listOf("naruto", "bleach"), awaitItem())
        }
    }

    @Test
    fun `WHEN query submitted SHOULD save it to history`() = runTest(dispatcher) {
        val viewModel = SearchHistoryViewModel(historySearchRepository)

        viewModel.onQuerySubmitted("naruto")
        advanceUntilIdle()

        coVerify { historySearchRepository.addSearchHistory("naruto") }
    }

    @Test
    fun `WHEN item removed SHOULD remove it from history`() = runTest(dispatcher) {
        val viewModel = SearchHistoryViewModel(historySearchRepository)

        viewModel.onRemoveClicked("naruto")
        advanceUntilIdle()

        coVerify { historySearchRepository.removeSearchHistory("naruto") }
    }
}
