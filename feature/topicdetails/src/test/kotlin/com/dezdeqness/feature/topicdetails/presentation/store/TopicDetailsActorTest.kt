package com.dezdeqness.feature.topicdetails.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.repository.AnimeRepository
import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.domain.model.CharacterDetailsEntity
import com.dezdeqness.domain.repository.CharacterRepository
import com.dezdeqness.feature.topicdetails.presentation.LinkedEntityComposer
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityUiModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_ANIME
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_CHARACTER
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class TopicDetailsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var topicRepository: TopicRepository

    @MockK
    lateinit var animeRepository: AnimeRepository

    @MockK
    lateinit var characterRepository: CharacterRepository

    @MockK
    lateinit var linkedEntityComposer: LinkedEntityComposer

    @MockK
    lateinit var topicPresentationComposer: TopicPresentationComposer

    private lateinit var actor: TopicDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = TopicDetailsActor(
            topicRepository = topicRepository,
            animeRepository = animeRepository,
            characterRepository = characterRepository,
            linkedEntityComposer = linkedEntityComposer,
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
    fun `WHEN LoadLinkedEntity for Anime succeeds SHOULD emit OnLinkedEntityLoaded`() = runTest {
        val animeEntity = mockk<AnimeDetailsEntity>()
        val animeUi = LinkedEntityUiModel.Anime(
            id = 1L,
            imageUrl = "image",
            title = "Anime",
            status = "status",
            type = "type",
        )

        coEvery { animeRepository.getDetails(id = 7L, isAuthorized = false) } returns Result.success(animeEntity)
        every { linkedEntityComposer.compose(animeEntity) } returns animeUi

        actor.execute(TopicDetailsNamespace.Command.LoadLinkedEntity(7L, LINKED_TYPE_ANIME)).test {
            val event = awaitItem()
            assertTrue(event is TopicDetailsNamespace.Event.OnLinkedEntityLoaded)
            assertEquals(animeUi, (event as TopicDetailsNamespace.Event.OnLinkedEntityLoaded).entity)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadLinkedEntity for Anime fails SHOULD emit OnLinkedEntityLoadError`() = runTest {
        coEvery { animeRepository.getDetails(id = 7L, isAuthorized = false) } returns Result.failure(Exception())

        actor.execute(TopicDetailsNamespace.Command.LoadLinkedEntity(7L, LINKED_TYPE_ANIME)).test {
            assertTrue(awaitItem() is TopicDetailsNamespace.Event.OnLinkedEntityLoadError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadLinkedEntity for Character succeeds SHOULD emit OnLinkedEntityLoaded`() = runTest {
        val characterEntity = mockk<CharacterDetailsEntity>()
        val characterUi = LinkedEntityUiModel.Character(
            id = 9L,
            imageUrl = "image",
            title = "Character",
            url = "https://shikimori.io/characters/9",
        )

        coEvery { characterRepository.getCharacterDetailsById(id = 9L) } returns Result.success(characterEntity)
        every { linkedEntityComposer.compose(characterEntity) } returns characterUi

        actor.execute(TopicDetailsNamespace.Command.LoadLinkedEntity(9L, LINKED_TYPE_CHARACTER)).test {
            val event = awaitItem()
            assertTrue(event is TopicDetailsNamespace.Event.OnLinkedEntityLoaded)
            assertEquals(characterUi, (event as TopicDetailsNamespace.Event.OnLinkedEntityLoaded).entity)
            awaitComplete()
        }
    }
}
