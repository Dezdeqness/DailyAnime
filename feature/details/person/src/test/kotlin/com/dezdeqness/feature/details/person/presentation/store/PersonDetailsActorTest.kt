package com.dezdeqness.feature.details.person.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.AccountType
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.contract.person.model.PersonDetailsEntity
import com.dezdeqness.contract.person.repository.PersonRepository
import com.dezdeqness.domain.usecases.FetchFavouritesUseCase
import com.dezdeqness.domain.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.person.presentation.composer.PersonDetailsComposer
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.test.MainDispatcherExtension
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
class PersonDetailsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var personRepository: PersonRepository

    @MockK
    lateinit var composer: PersonDetailsComposer

    @MockK(relaxed = true)
    lateinit var observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCase

    @MockK(relaxed = true)
    lateinit var fetchFavouritesUseCase: FetchFavouritesUseCase

    @MockK(relaxed = true)
    lateinit var favouriteRepository: FavouriteRepository

    @MockK(relaxed = true)
    lateinit var sessionManager: SessionManager

    private lateinit var actor: PersonDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = PersonDetailsActor(
            personRepository = personRepository,
            composer = composer,
            observeFavouriteStatusUseCase = observeFavouriteStatusUseCase,
            fetchFavouritesUseCase = fetchFavouritesUseCase,
            favouriteRepository = favouriteRepository,
            sessionManager = sessionManager,
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

    @Test
    fun `WHEN ToggleFavourite succeeds for authorized user SHOULD emit FavouriteToggleSucceeded`() = runTest {
        every { sessionManager.currentSession } returns authenticatedSession(userId = 42L)
        coEvery {
            favouriteRepository.toggleFavourite(
                userId = 42L,
                targetId = 7L,
                type = FavouriteLinkedType.PERSON,
                kind = FavouriteKind.SEYU,
            )
        } returns Result.success(Unit)

        actor.execute(
            PersonDetailsNamespace.Command.Base(
                BaseDetailsCommand.ToggleFavourite(
                    targetId = 7L,
                    type = FavouriteLinkedType.PERSON,
                    kind = FavouriteKind.SEYU,
                ),
            ),
        ).test {
            val event = awaitItem()
            assertTrue(event is PersonDetailsNamespace.Event.Base)
            event as PersonDetailsNamespace.Event.Base
            assertTrue(event.event is BaseDetailsEvent.FavouriteToggleSucceeded)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN ToggleFavourite fails for authorized user SHOULD emit FavouriteToggleFailed`() = runTest {
        every { sessionManager.currentSession } returns authenticatedSession(userId = 42L)
        val error = RuntimeException("network")
        coEvery {
            favouriteRepository.toggleFavourite(
                userId = 42L,
                targetId = 7L,
                type = FavouriteLinkedType.PERSON,
                kind = FavouriteKind.SEYU,
            )
        } returns Result.failure(error)

        actor.execute(
            PersonDetailsNamespace.Command.Base(
                BaseDetailsCommand.ToggleFavourite(
                    targetId = 7L,
                    type = FavouriteLinkedType.PERSON,
                    kind = FavouriteKind.SEYU,
                ),
            ),
        ).test {
            val event = awaitItem()
            assertTrue(event is PersonDetailsNamespace.Event.Base)
            event as PersonDetailsNamespace.Event.Base
            val inner = event.event
            assertTrue(inner is BaseDetailsEvent.FavouriteToggleFailed)
            assertEquals(error, (inner as BaseDetailsEvent.FavouriteToggleFailed).error)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN ToggleFavourite invoked without session SHOULD emit nothing`() = runTest {
        every { sessionManager.currentSession } returns null

        actor.execute(
            PersonDetailsNamespace.Command.Base(
                BaseDetailsCommand.ToggleFavourite(
                    targetId = 7L,
                    type = FavouriteLinkedType.PERSON,
                    kind = FavouriteKind.SEYU,
                ),
            ),
        ).test {
            awaitComplete()
        }
        coVerify(exactly = 0) {
            favouriteRepository.toggleFavourite(any(), any(), any(), any())
        }
    }

    @Test
    fun `WHEN FetchFavourites invoked for authorized user SHOULD call use case`() = runTest {
        every { sessionManager.currentSession } returns authenticatedSession(userId = 42L)
        coEvery { fetchFavouritesUseCase(userId = 42L, force = true) } returns Result.success(Unit)

        actor.execute(
            PersonDetailsNamespace.Command.Base(BaseDetailsCommand.FetchFavourites(force = true)),
        ).test {
            awaitComplete()
        }
        coVerify(exactly = 1) { fetchFavouritesUseCase(userId = 42L, force = true) }
    }

    @Test
    fun `WHEN FetchFavourites invoked without session SHOULD not call use case`() = runTest {
        every { sessionManager.currentSession } returns null

        actor.execute(
            PersonDetailsNamespace.Command.Base(BaseDetailsCommand.FetchFavourites()),
        ).test {
            awaitComplete()
        }
        coVerify(exactly = 0) { fetchFavouritesUseCase(any(), any()) }
    }

    private fun authenticatedSession(userId: Long) = SessionState.Authenticated(
        userId = userId,
        nickname = "tester",
        avatar = "",
        accountType = AccountType.SHIKIMORI,
    )
}
