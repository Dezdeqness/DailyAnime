package com.dezdeqness.feature.details.anime.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity
import com.dezdeqness.contract.anime.model.ScreenshotEntity
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.user.model.StatsItemEntity
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEffect
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus
import com.dezdeqness.feature.userrate.EditRateUiModel
import com.dezdeqness.foundation.test.MainDispatcherExtension
import io.mockk.MockKAnnotations
import io.mockk.every
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
class AnimeDetailsReducerTest {

    @MockK(relaxed = true)
    private lateinit var actor: AnimeDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN Base InitialLoad SHOULD set Loading state and emit LoadDetails command`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State())

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.InitialLoad(id = 42L)))

            val loading = awaitItem()
            assertEquals(
                AnimeDetailsNamespace.State(id = 42L, status = DetailsStatus.Loading),
                loading,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnDetailsLoaded SHOULD set Loaded with sections, title, shareUrl`() = runTest {
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { russian } returns "Берсерк"
            every { url } returns "/animes/33"
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
        }
        val sections = listOf(mockk<AnimeDetailsSection>())

        val store = newStore(AnimeDetailsNamespace.State(id = 33L, status = DetailsStatus.Loading))

        store.states.drop(1).test {
            store.accept(
                AnimeDetailsNamespace.Event.OnDetailsLoaded(
                    details = full,
                    sections = sections,
                    isAuthorized = true,
                ),
            )

            val loaded = awaitItem()
            assertEquals(DetailsStatus.Loaded, loaded.status)
            assertEquals(full, loaded.details)
            assertEquals(sections, loaded.sections)
            assertEquals("Берсерк", loaded.title)
            assertEquals("/animes/33", loaded.shareUrl)
            assertEquals(true, loaded.isAuthorized)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base OnDetailsLoadError SHOULD set Error state and emit Error effect`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loading))

        store.effects.test {
            store.accept(
                AnimeDetailsNamespace.Event.Base(
                    BaseDetailsEvent.OnDetailsLoadError(message = "boom", error = Throwable()),
                ),
            )

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.Base(BaseDetailsEffect.Error), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base RetryClicked AND state is Loading SHOULD do nothing`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loading))

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base SharePressed AND shareUrl is empty SHOULD do nothing`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L, shareUrl = ""))

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN Base SharePressed AND shareUrl is present SHOULD emit Share effect`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L, shareUrl = "/animes/1"))

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed))

            val effect = awaitItem()
            assertEquals(
                AnimeDetailsNamespace.Effect.Base(BaseDetailsEffect.Share("/animes/1")),
                effect,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN EditRateClicked AND details present SHOULD show Visible sheet`() = runTest {
        val userRate = mockk<UserRateEntity> { every { id } returns 99L }
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { russian } returns "Title"
            every { this@mockk.userRate } returns userRate
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.EditRateClicked)

            val next = awaitItem()
            assertEquals(
                EditRateSheetState.Visible(userRateId = 99L, title = "Title"),
                next.editRateSheet,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN EditRateClicked AND no details SHOULD do nothing`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L, details = null))

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.EditRateClicked)
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN EditRateClicked AND no userRate SHOULD use -1 as id`() = runTest {
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { russian } returns "Title"
            every { userRate } returns null
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.EditRateClicked)

            val next = awaitItem()
            assertEquals(
                EditRateSheetState.Visible(userRateId = -1L, title = "Title"),
                next.editRateSheet,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN EditRateClosed SHOULD set sheet to None`() = runTest {
        val store = newStore(
            AnimeDetailsNamespace.State(
                id = 1L,
                editRateSheet = EditRateSheetState.Visible(userRateId = 1L, title = "x"),
            ),
        )

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.EditRateClosed)

            val next = awaitItem()
            assertEquals(EditRateSheetState.None, next.editRateSheet)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN SaveUserRate SHOULD reset sheet and emit CreateOrUpdateUserRate command`() = runTest {
        val model = mockk<EditRateUiModel>()
        val store = newStore(
            AnimeDetailsNamespace.State(
                id = 7L,
                editRateSheet = EditRateSheetState.Visible(userRateId = 1L, title = "x"),
            ),
        )

        store.states.drop(1).test {
            store.accept(AnimeDetailsNamespace.Event.SaveUserRate(model = model))

            val next = awaitItem()
            assertEquals(EditRateSheetState.None, next.editRateSheet)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnUserRateSaved AND isCreate true SHOULD emit EditRateCreated effect`() = runTest {
        val rate = mockk<UserRateEntity>()
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { copy(userRate = rate) } returns this
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
            every { copy(animeDetailsEntity = animeEntity) } returns this
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.OnUserRateSaved(isCreate = true, rate = rate))

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.EditRateCreated(rate), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnUserRateSaved AND isCreate false SHOULD emit EditRateUpdated effect`() = runTest {
        val rate = mockk<UserRateEntity>()
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { copy(userRate = rate) } returns this
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
            every { copy(animeDetailsEntity = animeEntity) } returns this
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.OnUserRateSaved(isCreate = false, rate = rate))

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.EditRateUpdated(rate), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN OnUserRateSaveError SHOULD emit EditRateError effect`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L))

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.OnUserRateSaveError)

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.EditRateError, effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN StatsClicked AND details present SHOULD emit NavigateToStats`() = runTest {
        val scores = listOf(mockk<StatsItemEntity>())
        val statuses = listOf(mockk<StatsItemEntity>(), mockk<StatsItemEntity>())
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { scoresStats } returns scores
            every { statusesStats } returns statuses
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.StatsClicked)

            val effect = awaitItem()
            assertEquals(
                AnimeDetailsNamespace.Effect.NavigateToStats(scores = scores, statuses = statuses),
                effect,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN SimilarClicked SHOULD emit NavigateToSimilar`() = runTest {
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { id } returns 55L
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 55L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.SimilarClicked)

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.NavigateToSimilar(55L), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN ChronologyClicked SHOULD emit NavigateToChronology`() = runTest {
        val animeEntity = mockk<AnimeDetailsEntity>(relaxed = true) {
            every { id } returns 77L
        }
        val full = mockk<AnimeDetailsFullEntity> {
            every { animeDetailsEntity } returns animeEntity
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 77L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.ChronologyClicked)

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.NavigateToChronology(77L), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN RelatedClicked SHOULD emit NavigateToAnime with related id`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L))

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.RelatedClicked(animeId = 123L))

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.NavigateToAnime(123L), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN CharacterClicked SHOULD emit NavigateToCharacter`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L))

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.CharacterClicked(characterId = 321L))

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.NavigateToCharacter(321L), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN ScreenshotClicked AND preview matches SHOULD emit NavigateToScreenshotViewer`() = runTest {
        val s0 = ScreenshotEntity(original = "orig0", preview = "p0")
        val s1 = ScreenshotEntity(original = "orig1", preview = "p1")
        val full = mockk<AnimeDetailsFullEntity> {
            every { screenshots } returns listOf(s0, s1)
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.ScreenshotClicked(previewUrl = "host/p1"))

            val effect = awaitItem()
            assertEquals(
                AnimeDetailsNamespace.Effect.NavigateToScreenshotViewer(
                    index = 1,
                    urls = listOf("orig0", "orig1"),
                ),
                effect,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN ScreenshotClicked AND preview not found SHOULD do nothing`() = runTest {
        val full = mockk<AnimeDetailsFullEntity> {
            every { screenshots } returns listOf(ScreenshotEntity(original = "o", preview = "p"))
        }
        val store = newStore(
            AnimeDetailsNamespace.State(id = 1L, status = DetailsStatus.Loaded, details = full),
        )

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.ScreenshotClicked(previewUrl = "unknown"))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN VideoClicked SHOULD emit OpenVideo`() = runTest {
        val store = newStore(AnimeDetailsNamespace.State(id = 1L))

        store.effects.test {
            store.accept(AnimeDetailsNamespace.Event.VideoClicked(url = "https://video"))

            val effect = awaitItem()
            assertEquals(AnimeDetailsNamespace.Effect.OpenVideo("https://video"), effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun newStore(initial: AnimeDetailsNamespace.State) = ElmStore(
        initialState = initial,
        reducer = animeDetailsReducer,
        actor = actor,
    )
}
