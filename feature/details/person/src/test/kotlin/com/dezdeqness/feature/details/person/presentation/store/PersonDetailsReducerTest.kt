package com.dezdeqness.feature.details.person.presentation.store

import app.cash.turbine.test
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.foundation.test.MainDispatcherExtension
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import money.vivid.elmslie.core.store.ElmStore
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PersonDetailsReducerTest {

    @MockK(relaxed = true)
    private lateinit var actor: PersonDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN Base InitialLoad SHOULD set Loading state`() = runTest {
        val store = newStore(PersonDetailsNamespace.State())

        store.states.drop(1).test {
            store.accept(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.InitialLoad(id = 42L)))

            val loading = awaitItem()
            assertEquals(
                PersonDetailsNamespace.State(id = 42L, status = DetailsStatus.Loading),
                loading,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnDetailsLoaded SHOULD set Loaded with title, shareUrl, sections`() = runTest {
        val sections = listOf(mockk<DetailsSection>())
        val store = newStore(PersonDetailsNamespace.State(id = 1L, status = DetailsStatus.Loading))

        store.states.drop(1).test {
            store.accept(
                PersonDetailsNamespace.Event.OnDetailsLoaded(
                    title = "Natsuki",
                    shareUrl = "/people/1",
                    sections = sections,
                ),
            )

            val loaded = awaitItem()
            assertEquals(DetailsStatus.Loaded, loaded.status)
            assertEquals("Natsuki", loaded.title)
            assertEquals("/people/1", loaded.shareUrl)
            assertEquals(sections, loaded.sections)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base OnDetailsLoadError SHOULD set Error state and emit Error effect`() = runTest {
        val store = newStore(PersonDetailsNamespace.State(id = 1L, status = DetailsStatus.Loading))

        store.effects.test {
            store.accept(
                PersonDetailsNamespace.Event.Base(
                    BaseDetailsEvent.OnDetailsLoadError(message = "boom", error = Throwable()),
                ),
            )

            val effect = awaitItem()
            assertEquals(PersonDetailsNamespace.Effect.Base(BaseDetailsEffect.Error), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base RetryClicked AND state is Loading SHOULD do nothing`() = runTest {
        val store = newStore(PersonDetailsNamespace.State(id = 1L, status = DetailsStatus.Loading))

        store.states.drop(1).test {
            store.accept(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base SharePressed AND shareUrl empty SHOULD do nothing`() = runTest {
        val store = newStore(PersonDetailsNamespace.State(id = 1L, shareUrl = ""))

        store.effects.test {
            store.accept(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base SharePressed AND shareUrl present SHOULD emit Share effect`() = runTest {
        val store = newStore(PersonDetailsNamespace.State(id = 1L, shareUrl = "/people/1"))

        store.effects.test {
            store.accept(PersonDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed))

            val effect = awaitItem()
            assertEquals(
                PersonDetailsNamespace.Effect.Base(BaseDetailsEffect.Share("/people/1")),
                effect,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun newStore(initial: PersonDetailsNamespace.State) = ElmStore(
        initialState = initial,
        reducer = personDetailsReducer,
        actor = actor,
    )
}
