package com.dezdeqness.presentation.features.history.store

import app.cash.turbine.test
import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.presentation.features.history.HistoryComposer
import com.dezdeqness.presentation.features.history.models.HistoryModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class HistoryActorTest {

    @MockK(relaxed = true)
    lateinit var appLogger: AppLogger

    @MockK
    lateinit var getHistoryUseCase: GetHistoryUseCase

    @MockK
    lateinit var historyComposer: HistoryComposer

    private lateinit var actor: HistoryActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = HistoryActor(getHistoryUseCase, historyComposer, appLogger)
    }

    @Test
    fun `WHEN LoadPage is successful and isLoadMore is false SHOULD emits OnPageLoaded`() = runTest {
        val page = 1
        val command = HistoryNamespace.Command.LoadPage(page = page, isLoadMore = false)

        val rawItems = listOf(mockk<HistoryEntity>())
        val composedItems = listOf(mockk<HistoryModel>())
        val result = Result.success(GetHistoryUseCase.HistoryListState(rawItems, hasNextPage = true))

        every { getHistoryUseCase.invoke(page) } returns result
        every { historyComposer.compose(rawItems) } returns composedItems

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is HistoryNamespace.Event.OnPageLoaded)
            event as HistoryNamespace.Event.OnPageLoaded
            assertEquals(composedItems, event.list)
            assertTrue(event.hasNextPage)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage is successful and isLoadMore is true SHOULD emits OnLoadMorePageLoaded`() = runTest {
        val page = 2
        val command = HistoryNamespace.Command.LoadPage(page = page, isLoadMore = true)

        val rawItems = listOf(mockk<HistoryEntity>())
        val composedItems = listOf(mockk<HistoryModel>())
        val result = Result.success(GetHistoryUseCase.HistoryListState(rawItems, hasNextPage = false))

        every { getHistoryUseCase.invoke(page) } returns result
        every { historyComposer.compose(rawItems) } returns composedItems

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is HistoryNamespace.Event.OnLoadMorePageLoaded)
            event as HistoryNamespace.Event.OnLoadMorePageLoaded
            assertEquals(composedItems, event.list)
            assertFalse(event.hasNextPage)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage fails and isLoadMore is false SHOULD emits OnLoadPageError`() = runTest {
        val command = HistoryNamespace.Command.LoadPage(page = 1, isLoadMore = false)

        every { getHistoryUseCase.invoke(command.page) } returns Result.failure(Exception())

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is HistoryNamespace.Event.OnLoadPageError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage fails and isLoadMore is true SHOULD emits OnLoadMorePageError`() = runTest {
        val command = HistoryNamespace.Command.LoadPage(page = 1, isLoadMore = true)

        every { getHistoryUseCase.invoke(command.page) } returns Result.failure(Exception())

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is HistoryNamespace.Event.OnLoadMorePageError)
            awaitComplete()
        }
    }

}
