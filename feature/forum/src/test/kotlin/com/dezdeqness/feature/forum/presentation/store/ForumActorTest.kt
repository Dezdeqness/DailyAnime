package com.dezdeqness.feature.forum.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.forum.model.ForumEntity
import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.domain.usecases.GetForumsUseCase
import com.dezdeqness.feature.forum.presentation.ForumComposer
import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class ForumActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var getForumsUseCase: GetForumsUseCase

    @MockK
    lateinit var topicRepository: TopicRepository

    @MockK
    lateinit var forumComposer: ForumComposer

    @MockK
    lateinit var topicPresentationComposer: TopicPresentationComposer

    private lateinit var actor: ForumActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = ForumActor(
            getForumsUseCase = getForumsUseCase,
            topicRepository = topicRepository,
            forumComposer = forumComposer,
            topicPresentationComposer = topicPresentationComposer,
            logger = logger,
        )
    }

    @Test
    fun `WHEN Load is successful SHOULD emit OnLoaded with composed items`() = runTest {
        val rawForums = listOf(mockk<ForumEntity>())
        val composedForums = listOf(mockk<ForumUiModel>())

        every { getForumsUseCase.invoke() } returns Result.success(rawForums)
        every { forumComposer.compose(rawForums) } returns composedForums

        actor.execute(ForumNamespace.Command.Load).test {
            val event = awaitItem()
            assertTrue(event is ForumNamespace.Event.OnLoaded)
            event as ForumNamespace.Event.OnLoaded
            assertEquals(composedForums, event.list)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN Load fails SHOULD emit OnError`() = runTest {
        every { getForumsUseCase.invoke() } returns Result.failure(Exception())

        actor.execute(ForumNamespace.Command.Load).test {
            val event = awaitItem()
            assertTrue(event is ForumNamespace.Event.OnError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadHotTopics is successful SHOULD emit OnHotTopicsLoaded with composed topics`() = runTest {
        val rawTopics = listOf(mockk<TopicEntity>())
        val composedTopics = listOf(mockk<TopicPresentationModel>())

        every { topicRepository.getHotTopics(limit = 5) } returns Result.success(rawTopics)
        every { topicPresentationComposer.compose(rawTopics) } returns composedTopics

        actor.execute(ForumNamespace.Command.LoadHotTopics).test {
            val event = awaitItem()
            assertTrue(event is ForumNamespace.Event.OnHotTopicsLoaded)
            event as ForumNamespace.Event.OnHotTopicsLoaded
            assertEquals(composedTopics, event.topics)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadHotTopics fails SHOULD emit OnHotTopicsError`() = runTest {
        every { topicRepository.getHotTopics(limit = 5) } returns Result.failure(Exception())

        actor.execute(ForumNamespace.Command.LoadHotTopics).test {
            val event = awaitItem()
            assertTrue(event is ForumNamespace.Event.OnHotTopicsError)
            awaitComplete()
        }
    }
}
