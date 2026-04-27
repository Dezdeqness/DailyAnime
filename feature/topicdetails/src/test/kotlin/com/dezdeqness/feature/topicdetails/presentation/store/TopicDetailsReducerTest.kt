package com.dezdeqness.feature.topicdetails.presentation.store

import app.cash.turbine.test
import com.dezdeqness.feature.topicdetails.presentation.models.BaseRelatedAnime
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedAnimeUiModel
import com.dezdeqness.foundation.test.MainDispatcherExtension
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import money.vivid.elmslie.core.store.ElmStore
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TopicDetailsReducerTest {

    @MockK(relaxed = true)
    private lateinit var actor: TopicDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN InitialLoad SHOULD show loading and request topic`(): Unit = runTest {
        val store = ElmStore(
            initialState = TopicDetailsNamespace.State(),
            reducer = topicDetailsReducer,
            actor = actor,
        )

        every { actor.execute(TopicDetailsNamespace.Command.LoadTopic(topicId = 42L)) } returns flow { }

        store.states.drop(1).test {
            store.accept(TopicDetailsNamespace.Event.InitialLoad(42L))

            assertEquals(
                TopicDetailsNamespace.State(
                    topicId = 42L,
                    status = TopicDetailsStatus.Loading,
                    relatedAnime = BaseRelatedAnime.Initial,
                ),
                awaitItem(),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnTopicLoaded with linked anime SHOULD set loaded and request related anime`(): Unit = runTest {
        val topic = TopicPresentationModel(
            topicId = 42L,
            title = "Title",
            userNickname = "User",
            userAvatarUrl = "avatar",
            date = "date",
            commentsCount = 0,
            contentBlocks = emptyList(),
            footerBlocks = emptyList(),
            linkedId = 10L,
        )

        val store = ElmStore(
            initialState = TopicDetailsNamespace.State(status = TopicDetailsStatus.Loading),
            reducer = topicDetailsReducer,
            actor = actor,
        )

        every { actor.execute(TopicDetailsNamespace.Command.LoadRelatedAnime(animeId = 10L)) } returns flow { }

        store.states.drop(1).test {
            store.accept(TopicDetailsNamespace.Event.OnTopicLoaded(topic))

            assertEquals(
                TopicDetailsNamespace.State(
                    topicId = 42L,
                    topic = topic,
                    status = TopicDetailsStatus.Loaded,
                    relatedAnime = BaseRelatedAnime.Loading,
                ),
                awaitItem(),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnTopicLoaded without linked anime SHOULD set loaded and keep related anime empty`(): Unit = runTest {
        val topic = TopicPresentationModel(
            topicId = 42L,
            title = "Title",
            userNickname = "User",
            userAvatarUrl = "avatar",
            date = "date",
            commentsCount = 0,
            contentBlocks = emptyList(),
            footerBlocks = emptyList(),
            linkedId = null,
        )

        val store = ElmStore(
            initialState = TopicDetailsNamespace.State(status = TopicDetailsStatus.Loading),
            reducer = topicDetailsReducer,
            actor = actor,
        )

        store.states.drop(1).test {
            store.accept(TopicDetailsNamespace.Event.OnTopicLoaded(topic))

            assertEquals(
                TopicDetailsNamespace.State(
                    topicId = 42L,
                    topic = topic,
                    status = TopicDetailsStatus.Loaded,
                    relatedAnime = BaseRelatedAnime.Empty,
                ),
                awaitItem(),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnRelatedAnimeLoaded SHOULD update related anime state`(): Unit = runTest {
        val topic = mockk<TopicPresentationModel>()
        val anime = LinkedAnimeUiModel(
            id = 10L,
            imageUrl = "image",
            title = "Anime",
            status = "status",
            type = "type",
        )
        val initialState = TopicDetailsNamespace.State(
            topicId = 42L,
            topic = topic,
            status = TopicDetailsStatus.Loaded,
            relatedAnime = BaseRelatedAnime.Loading,
        )
        val store = ElmStore(
            initialState = initialState,
            reducer = topicDetailsReducer,
            actor = actor,
        )

        store.states.drop(1).test {
            store.accept(TopicDetailsNamespace.Event.OnRelatedAnimeLoaded(anime))

            assertEquals(
                initialState.copy(relatedAnime = BaseRelatedAnime.Loaded(anime)),
                awaitItem(),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnTopicLoadError SHOULD show error and emit effect`(): Unit = runTest {
        val store = ElmStore(
            initialState = TopicDetailsNamespace.State(status = TopicDetailsStatus.Loading),
            reducer = topicDetailsReducer,
            actor = actor,
        )

        store.effects.test {
            store.accept(TopicDetailsNamespace.Event.OnTopicLoadError("error", Throwable()))

            assertEquals(TopicDetailsNamespace.Effect.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
