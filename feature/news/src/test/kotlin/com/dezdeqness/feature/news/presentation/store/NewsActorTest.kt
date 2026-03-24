package com.dezdeqness.feature.news.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.domain.usecases.GetTopicUseCase
import com.dezdeqness.feature.news.presentation.NewsComposer
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
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
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class NewsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var getTopicUseCase: GetTopicUseCase

    @MockK
    lateinit var newsComposer: NewsComposer

    private lateinit var actor: NewsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = NewsActor(getTopicUseCase, newsComposer, logger)
    }

    @Test
    fun `WHEN LoadPage is successful and isLoadMore is false SHOULD emit OnPageLoaded`() = runTest {
        val page = 1
        val command = NewsNamespace.Command.LoadPage(page = page, isLoadMore = false)

        val rawItems = listOf(mockk<TopicEntity>())
        val composedItems = listOf(mockk<NewsUiModel>())
        val result = Result.success(
            GetTopicUseCase.NewsListState(rawItems, hasNextPage = true)
        )

        every { getTopicUseCase.invoke(forumType = "news", pageNumber = page) } returns result
        every { newsComposer.compose(rawItems) } returns composedItems

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is NewsNamespace.Event.OnPageLoaded)
            event as NewsNamespace.Event.OnPageLoaded
            assertEquals(composedItems, event.list)
            assertTrue(event.hasNextPage)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage is successful and isLoadMore is true SHOULD emit OnLoadMorePageLoaded`() = runTest {
        val page = 2
        val command = NewsNamespace.Command.LoadPage(page = page, isLoadMore = true)

        val rawItems = listOf(mockk<TopicEntity>())
        val composedItems = listOf(mockk<NewsUiModel>())
        val result = Result.success(
            GetTopicUseCase.NewsListState(rawItems, hasNextPage = false)
        )

        every { getTopicUseCase.invoke(forumType = "news", pageNumber = page) } returns result
        every { newsComposer.compose(rawItems) } returns composedItems

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is NewsNamespace.Event.OnLoadMorePageLoaded)
            event as NewsNamespace.Event.OnLoadMorePageLoaded
            assertEquals(composedItems, event.list)
            assertFalse(event.hasNextPage)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage fails and isLoadMore is false SHOULD emit OnLoadPageError`() = runTest {
        val command = NewsNamespace.Command.LoadPage(page = 1, isLoadMore = false)

        every {
            getTopicUseCase.invoke(forumType = "news", pageNumber = command.page)
        } returns Result.failure(Exception())

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is NewsNamespace.Event.OnLoadPageError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage fails and isLoadMore is true SHOULD emit OnLoadMorePageError`() = runTest {
        val command = NewsNamespace.Command.LoadPage(page = 2, isLoadMore = true)

        every {
            getTopicUseCase.invoke(forumType = "news", pageNumber = command.page)
        } returns Result.failure(Exception())

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is NewsNamespace.Event.OnLoadMorePageError)
            awaitComplete()
        }
    }
}
