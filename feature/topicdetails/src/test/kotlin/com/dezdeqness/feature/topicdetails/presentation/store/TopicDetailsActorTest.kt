package com.dezdeqness.feature.topicdetails.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.feature.topicdetails.presentation.LinkedAnimeComposer
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedAnimeUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class TopicDetailsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var topicRepository: TopicRepository

    @MockK
    lateinit var animeRepository: AnimeRepository

    @MockK
    lateinit var linkedAnimeComposer: LinkedAnimeComposer

    @MockK
    lateinit var topicPresentationComposer: TopicPresentationComposer

    private lateinit var actor: TopicDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = TopicDetailsActor(
            topicRepository = topicRepository,
            animeRepository = animeRepository,
            linkedAnimeComposer = linkedAnimeComposer,
            topicPresentationComposer = topicPresentationComposer,
            logger = logger,
        )
    }

    @Test
    fun `WHEN LoadTopic succeeds SHOULD emit OnTopicLoaded`() = runTest {
        val topicEntity = mockk<TopicEntity>()
        val topicPresentation = mockk<TopicPresentationModel>()

        coEvery { topicRepository.getTopicsById(42) } returns Result.success(topicEntity)
        coEvery { topicPresentationComposer.compose(topicEntity) } returns topicPresentation

        actor.execute(TopicDetailsNamespace.Command.LoadTopic(42L)).test {
            val event = awaitItem()
            assertTrue(event is TopicDetailsNamespace.Event.OnTopicLoaded)
            assertEquals(topicPresentation, (event as TopicDetailsNamespace.Event.OnTopicLoaded).topic)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadTopic fails SHOULD emit OnTopicLoadError`() = runTest {
        coEvery { topicRepository.getTopicsById(42) } returns Result.failure(Exception())

        actor.execute(TopicDetailsNamespace.Command.LoadTopic(42L)).test {
            assertTrue(awaitItem() is TopicDetailsNamespace.Event.OnTopicLoadError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadRelatedAnime succeeds SHOULD emit OnRelatedAnimeLoaded`() = runTest {
        val animeEntity = mockk<AnimeDetailsEntity>()
        val animeUi = LinkedAnimeUiModel(
            id = 1L,
            imageUrl = "image",
            title = "Anime",
            status = "status",
            type = "type",
        )

        coEvery { animeRepository.getDetails(id = 7L, isAuthorized = false) } returns Result.success(animeEntity)
        coEvery { linkedAnimeComposer.compose(animeEntity) } returns animeUi

        actor.execute(TopicDetailsNamespace.Command.LoadRelatedAnime(7L)).test {
            val event = awaitItem()
            assertTrue(event is TopicDetailsNamespace.Event.OnRelatedAnimeLoaded)
            assertEquals(animeUi, (event as TopicDetailsNamespace.Event.OnRelatedAnimeLoaded).anime)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadRelatedAnime fails SHOULD emit OnRelatedAnimeLoadError`() = runTest {
        coEvery { animeRepository.getDetails(id = 7L, isAuthorized = false) } returns Result.failure(Exception())

        actor.execute(TopicDetailsNamespace.Command.LoadRelatedAnime(7L)).test {
            assertTrue(awaitItem() is TopicDetailsNamespace.Event.OnRelatedAnimeLoadError)
            awaitComplete()
        }
    }
}
