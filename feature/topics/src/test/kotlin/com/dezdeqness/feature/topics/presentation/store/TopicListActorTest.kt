package com.dezdeqness.feature.topics.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.usecases.GetTopicUseCase
import com.dezdeqness.feature.topics.presentation.TopicListComposer
import com.dezdeqness.feature.topics.presentation.models.TopicListUiModel
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
class TopicListActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var getTopicUseCase: GetTopicUseCase

    @MockK
    lateinit var topicListComposer: TopicListComposer

    private lateinit var actor: TopicListActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = TopicListActor(getTopicUseCase, topicListComposer, logger, "news")
    }

    @Test
    fun `WHEN LoadPage is successful and isLoadMore is false SHOULD emit OnPageLoaded`() = runTest {
        val page = 1
        val command = TopicListNamespace.Command.LoadPage(page = page, isLoadMore = false)

        val rawItems = listOf(mockk<TopicEntity>())
        val composedItems = listOf(mockk<TopicListUiModel>())
        val result = Result.success(
            GetTopicUseCase.NewsListState(rawItems, hasNextPage = true)
        )

        every { getTopicUseCase.invoke(forumType = "news", pageNumber = page) } returns result
        every { topicListComposer.compose(rawItems) } returns composedItems

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is TopicListNamespace.Event.OnPageLoaded)
            event as TopicListNamespace.Event.OnPageLoaded
            assertEquals(composedItems, event.list)
            assertTrue(event.hasNextPage)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage is successful and isLoadMore is true SHOULD emit OnLoadMorePageLoaded`() = runTest {
        val page = 2
        val command = TopicListNamespace.Command.LoadPage(page = page, isLoadMore = true)

        val rawItems = listOf(mockk<TopicEntity>())
        val composedItems = listOf(mockk<TopicListUiModel>())
        val result = Result.success(
            GetTopicUseCase.NewsListState(rawItems, hasNextPage = false)
        )

        every { getTopicUseCase.invoke(forumType = "news", pageNumber = page) } returns result
        every { topicListComposer.compose(rawItems) } returns composedItems

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is TopicListNamespace.Event.OnLoadMorePageLoaded)
            event as TopicListNamespace.Event.OnLoadMorePageLoaded
            assertEquals(composedItems, event.list)
            assertFalse(event.hasNextPage)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage fails and isLoadMore is false SHOULD emit OnLoadPageError`() = runTest {
        val command = TopicListNamespace.Command.LoadPage(page = 1, isLoadMore = false)

        every {
            getTopicUseCase.invoke(forumType = "news", pageNumber = command.page)
        } returns Result.failure(Exception())

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is TopicListNamespace.Event.OnLoadPageError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadPage fails and isLoadMore is true SHOULD emit OnLoadMorePageError`() = runTest {
        val command = TopicListNamespace.Command.LoadPage(page = 2, isLoadMore = true)

        every {
            getTopicUseCase.invoke(forumType = "news", pageNumber = command.page)
        } returns Result.failure(Exception())

        actor.execute(command).test {
            val event = awaitItem()
            assertTrue(event is TopicListNamespace.Event.OnLoadMorePageError)
            awaitComplete()
        }
    }
}
