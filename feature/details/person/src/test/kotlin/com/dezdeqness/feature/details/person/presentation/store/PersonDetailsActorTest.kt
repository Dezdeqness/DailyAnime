package com.dezdeqness.feature.details.person.presentation.store

import app.cash.turbine.test
import com.dezdeqness.domain.model.PersonDetailsEntity
import com.dezdeqness.domain.repository.PersonRepository
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.composer.PersonDetailsComposer
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
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
class PersonDetailsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var personRepository: PersonRepository

    @MockK
    lateinit var composer: PersonDetailsComposer

    private lateinit var actor: PersonDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = PersonDetailsActor(
            personRepository = personRepository,
            composer = composer,
            logger = logger,
        )
    }

    @Test
    fun `WHEN LoadDetails is successful AND russian present SHOULD use russian as title`() = runTest {
        val entity = mockk<PersonDetailsEntity>(relaxed = true) {
            every { russian } returns "Нацуки"
            every { name } returns "Natsuki"
            every { url } returns "/people/1"
        }
        val sections = listOf(mockk<DetailsSection>())

        coEvery { personRepository.getPersonDetailsById(1L) } returns Result.success(entity)
        every { composer.compose(entity) } returns sections

        actor.execute(
            PersonDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is PersonDetailsNamespace.Event.OnDetailsLoaded)
            event as PersonDetailsNamespace.Event.OnDetailsLoaded
            assertEquals("Нацуки", event.title)
            assertEquals("/people/1", event.shareUrl)
            assertEquals(sections, event.sections)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadDetails is successful AND russian empty SHOULD fallback to name`() = runTest {
        val entity = mockk<PersonDetailsEntity>(relaxed = true) {
            every { russian } returns ""
            every { name } returns "Natsuki"
            every { url } returns "/people/1"
        }

        coEvery { personRepository.getPersonDetailsById(1L) } returns Result.success(entity)
        every { composer.compose(entity) } returns emptyList()

        actor.execute(
            PersonDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is PersonDetailsNamespace.Event.OnDetailsLoaded)
            event as PersonDetailsNamespace.Event.OnDetailsLoaded
            assertEquals("Natsuki", event.title)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadDetails fails SHOULD emit Base OnDetailsLoadError`() = runTest {
        coEvery { personRepository.getPersonDetailsById(1L) } returns Result.failure(Exception())

        actor.execute(
            PersonDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is PersonDetailsNamespace.Event.Base)
            event as PersonDetailsNamespace.Event.Base
            assertTrue(event.event is BaseDetailsEvent.OnDetailsLoadError)
            awaitComplete()
        }
    }
}
