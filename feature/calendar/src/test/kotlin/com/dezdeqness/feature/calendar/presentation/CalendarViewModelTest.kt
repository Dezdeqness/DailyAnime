package com.dezdeqness.feature.calendar.presentation

import app.cash.turbine.test
import com.dezdeqness.contract.calendar.model.AnimeCalendarEntity
import com.dezdeqness.contract.calendar.repository.CalendarRepository
import com.dezdeqness.feature.calendar.presentation.models.CalendarListUiModel
import com.dezdeqness.feature.calendar.presentation.models.CalendarUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
class CalendarViewModelTest {

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var calendarRepository: CalendarRepository

    @MockK
    private lateinit var calendarComposer: CalendarComposer

    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        viewModel = CalendarViewModel(
            calendarRepository = calendarRepository,
            calendarComposer = calendarComposer,
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
    fun `WHEN calendar loaded successfully SHOULD emit loaded state with list`() = runTest {
        val entity = mockk<AnimeCalendarEntity>()
        val uiList = listOf(calendarSection())

        coEvery { calendarRepository.getCalendar() } returns Result.success(listOf(entity))
        every { calendarComposer.compose(listOf(entity), query = "") } returns uiList

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(uiList, state.list)
            assertEquals(CalendarStatus.Loaded, state.status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN calendar is empty SHOULD emit empty state`() = runTest {
        coEvery { calendarRepository.getCalendar() } returns Result.success(emptyList())
        every { calendarComposer.compose(emptyList(), query = "") } returns emptyList()

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(CalendarStatus.Empty, state.status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN initial loading fails SHOULD emit error state`() = runTest {
        val error = Exception("Network error")

        coEvery { calendarRepository.getCalendar() } returns Result.failure(error)

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(CalendarStatus.Error, state.status)

            cancelAndIgnoreRemainingEvents()
        }

        verify { logger.logInfo("CalendarViewModel", any(), error) }
    }

    @Test
    fun `WHEN query changed SHOULD filter cached items and request scroll`() = runTest {
        val entity = mockk<AnimeCalendarEntity>()
        val fullList = listOf(calendarSection())
        val filteredList = listOf(calendarSection(name = "Ван-Пис"))

        coEvery { calendarRepository.getCalendar() } returns Result.success(listOf(entity))
        every { calendarComposer.compose(listOf(entity), query = "") } returns fullList
        every { calendarComposer.compose(listOf(entity), query = "ван") } returns filteredList

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            viewModel.onQueryChanged("ван")
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(filteredList, state.list)
            assertTrue(viewModel.scrollNeedFlow.value)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN scrolled SHOULD reset scroll flag`() = runTest {
        val entity = mockk<AnimeCalendarEntity>()

        coEvery { calendarRepository.getCalendar() } returns Result.success(listOf(entity))
        every { calendarComposer.compose(listOf(entity), query = "") } returns listOf(calendarSection())
        every { calendarComposer.compose(listOf(entity), query = "query") } returns emptyList()

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            viewModel.onQueryChanged("query")
            advanceUntilIdle()

            viewModel.onScrolled()
            advanceUntilIdle()

            assertFalse(viewModel.scrollNeedFlow.value)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN pull down refreshed SHOULD reload list and hide indicator`() = runTest {
        val entity = mockk<AnimeCalendarEntity>()
        val initialList = listOf(calendarSection())
        val refreshedList = listOf(calendarSection(name = "Ван-Пис"))

        coEvery { calendarRepository.getCalendar() } returns Result.success(listOf(entity))
        every { calendarComposer.compose(listOf(entity), query = "") } returns initialList

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            every { calendarComposer.compose(listOf(entity), query = "") } returns refreshedList

            viewModel.onPullDownRefreshed()
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(refreshedList, state.list)
            assertFalse(viewModel.pullRefreshFlow.value)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN refresh fails SHOULD keep previous list`() = runTest {
        val entity = mockk<AnimeCalendarEntity>()
        val initialList = listOf(calendarSection())

        coEvery { calendarRepository.getCalendar() } returns Result.success(listOf(entity))
        every { calendarComposer.compose(listOf(entity), query = "") } returns initialList

        viewModel.calendarStateFlow.test {
            advanceUntilIdle()

            coEvery { calendarRepository.getCalendar() } returns Result.failure(Exception("Network error"))

            viewModel.onPullDownRefreshed()
            advanceUntilIdle()

            val state = expectMostRecentItem()

            assertEquals(initialList, state.list)
            assertEquals(CalendarStatus.Loaded, state.status)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun calendarSection(name: String = "Восстание Лелуша") = CalendarListUiModel(
        header = "Sunday, July 19",
        items = listOf(
            CalendarUiModel(
                id = 1L,
                name = name,
                ongoingEpisode = 12,
                type = "tv",
                score = "8.69",
                time = "12:55",
                logoUrl = "",
            ),
        ),
    )
}
