package com.dezdeqness.feature.details.character.presentation.store

import app.cash.turbine.test
import com.dezdeqness.domain.model.CharacterDetailsEntity
import com.dezdeqness.domain.repository.CharacterRepository
import com.dezdeqness.feature.details.character.presentation.composer.CharacterDetailsComposer
import com.dezdeqness.feature.details.character.presentation.models.CharacterDetailsSection
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class CharacterDetailsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var characterRepository: CharacterRepository

    @MockK
    lateinit var composer: CharacterDetailsComposer

    private lateinit var actor: CharacterDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = CharacterDetailsActor(
            characterRepository = characterRepository,
            composer = composer,
            logger = logger,
        )
    }

    @Test
    fun `WHEN LoadDetails is successful AND russian present SHOULD use russian as title`() = runTest {
        val entity = mockk<CharacterDetailsEntity>(relaxed = true) {
            every { russian } returns "Тандзиро"
            every { name } returns "Tanjiro"
            every { url } returns "/characters/1"
        }
        val sections = listOf(mockk<CharacterDetailsSection>())

        every { characterRepository.getCharacterDetailsById(1L) } returns Result.success(entity)
        every { composer.compose(entity) } returns sections

        actor.execute(
            CharacterDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is CharacterDetailsNamespace.Event.OnDetailsLoaded)
            event as CharacterDetailsNamespace.Event.OnDetailsLoaded
            assertEquals("Тандзиро", event.title)
            assertEquals("/characters/1", event.shareUrl)
            assertEquals(sections, event.sections)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadDetails is successful AND russian empty SHOULD fallback to name`() = runTest {
        val entity = mockk<CharacterDetailsEntity>(relaxed = true) {
            every { russian } returns ""
            every { name } returns "Tanjiro"
            every { url } returns "/characters/1"
        }

        every { characterRepository.getCharacterDetailsById(1L) } returns Result.success(entity)
        every { composer.compose(entity) } returns emptyList()

        actor.execute(
            CharacterDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is CharacterDetailsNamespace.Event.OnDetailsLoaded)
            event as CharacterDetailsNamespace.Event.OnDetailsLoaded
            assertEquals("Tanjiro", event.title)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadDetails fails SHOULD emit Base OnDetailsLoadError`() = runTest {
        every { characterRepository.getCharacterDetailsById(1L) } returns Result.failure(Exception())

        actor.execute(
            CharacterDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is CharacterDetailsNamespace.Event.Base)
            event as CharacterDetailsNamespace.Event.Base
            assertTrue(event.event is BaseDetailsEvent.OnDetailsLoadError)
            awaitComplete()
        }
    }
}
