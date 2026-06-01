package com.dezdeqness.feature.details.anime.presentation.store

import app.cash.turbine.test
import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.domain.usecases.CreateOrUpdateUserRateUseCase
import com.dezdeqness.domain.usecases.GetAnimeDetailsUseCase
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.AccountType
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.favourite.model.FavouriteKind
import com.dezdeqness.contract.favourite.model.FavouriteLinkedType
import com.dezdeqness.contract.favourite.repository.FavouriteRepository
import com.dezdeqness.domain.usecases.FetchFavouritesUseCase
import com.dezdeqness.domain.usecases.ObserveFavouriteStatusUseCase
import com.dezdeqness.feature.details.anime.presentation.composer.AnimeDetailsComposer
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsCommand
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.userrate.EditRateUiModel
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
class AnimeDetailsActorTest {

    @MockK(relaxed = true)
    lateinit var logger: Logger

    @MockK
    lateinit var getAnimeDetailsUseCase: GetAnimeDetailsUseCase

    @MockK
    lateinit var createOrUpdateUserRateUseCase: CreateOrUpdateUserRateUseCase

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var composer: AnimeDetailsComposer

    @MockK(relaxed = true)
    lateinit var observeFavouriteStatusUseCase: ObserveFavouriteStatusUseCase

    @MockK(relaxed = true)
    lateinit var fetchFavouritesUseCase: FetchFavouritesUseCase

    @MockK(relaxed = true)
    lateinit var favouriteRepository: FavouriteRepository

    @MockK(relaxed = true)
    lateinit var sessionManager: SessionManager

    private lateinit var actor: AnimeDetailsActor

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        actor = AnimeDetailsActor(
            getAnimeDetailsUseCase = getAnimeDetailsUseCase,
            createOrUpdateUserRateUseCase = createOrUpdateUserRateUseCase,
            authRepository = authRepository,
            composer = composer,
            observeFavouriteStatusUseCase = observeFavouriteStatusUseCase,
            fetchFavouritesUseCase = fetchFavouritesUseCase,
            favouriteRepository = favouriteRepository,
            sessionManager = sessionManager,
            logger = logger,
        )
    }

    @Test
    fun `WHEN LoadDetails is successful SHOULD emit OnDetailsLoaded with composed sections`() = runTest {
        val details = mockk<AnimeDetailsFullEntity>()
        val sections = listOf(mockk<AnimeDetailsSection>())

        coEvery { getAnimeDetailsUseCase.invoke(any()) } returns Result.success(details)
        every { composer.compose(details) } returns sections
        every { authRepository.isAuthorized() } returns true

        actor.execute(
            AnimeDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.OnDetailsLoaded)
            event as AnimeDetailsNamespace.Event.OnDetailsLoaded
            assertEquals(details, event.details)
            assertEquals(sections, event.sections)
            assertEquals(true, event.isAuthorized)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN LoadDetails fails SHOULD emit Base OnDetailsLoadError`() = runTest {
        coEvery { getAnimeDetailsUseCase.invoke(any()) } returns Result.failure(Exception("boom"))

        actor.execute(
            AnimeDetailsNamespace.Command.Base(BaseDetailsCommand.LoadDetails(id = 1L)),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.Base)
            event as AnimeDetailsNamespace.Event.Base
            assertTrue(event.event is BaseDetailsEvent.OnDetailsLoadError)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN CreateOrUpdateUserRate succeeds for new rate SHOULD emit OnUserRateSaved isCreate true`() = runTest {
        val model = EditRateUiModel(
            rateId = -1L,
            status = "planned",
            episodes = 0L,
            score = 0f,
            comment = "",
        )
        val rate = mockk<UserRateEntity>()
        every {
            createOrUpdateUserRateUseCase.invoke(
                rateId = any(),
                targetId = any(),
                status = any(),
                episodes = any(),
                score = any(),
                comment = any(),
            )
        } returns Result.success(rate)

        actor.execute(
            AnimeDetailsNamespace.Command.CreateOrUpdateUserRate(animeId = 9L, model = model),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.OnUserRateSaved)
            event as AnimeDetailsNamespace.Event.OnUserRateSaved
            assertEquals(true, event.isCreate)
            assertEquals(rate, event.rate)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN CreateOrUpdateUserRate succeeds for existing rate SHOULD emit OnUserRateSaved isCreate false`() = runTest {
        val model = EditRateUiModel(
            rateId = 100L,
            status = "watching",
            episodes = 5L,
            score = 8f,
            comment = "ok",
        )
        val rate = mockk<UserRateEntity>()
        every {
            createOrUpdateUserRateUseCase.invoke(
                rateId = any(),
                targetId = any(),
                status = any(),
                episodes = any(),
                score = any(),
                comment = any(),
            )
        } returns Result.success(rate)

        actor.execute(
            AnimeDetailsNamespace.Command.CreateOrUpdateUserRate(animeId = 9L, model = model),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.OnUserRateSaved)
            event as AnimeDetailsNamespace.Event.OnUserRateSaved
            assertEquals(false, event.isCreate)
            assertEquals(rate, event.rate)
            awaitComplete()
        }
    }

    @Test
    fun `WHEN CreateOrUpdateUserRate fails SHOULD emit OnUserRateSaveError`() = runTest {
        val model = EditRateUiModel(
            rateId = -1L,
            status = "planned",
            episodes = 0L,
            score = 0f,
            comment = "",
        )
        every {
            createOrUpdateUserRateUseCase.invoke(
                rateId = any(),
                targetId = any(),
                status = any(),
                episodes = any(),
                score = any(),
                comment = any(),
            )
        } returns Result.failure(Exception())

        actor.execute(
            AnimeDetailsNamespace.Command.CreateOrUpdateUserRate(animeId = 9L, model = model),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.OnUserRateSaveError)
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
                type = FavouriteLinkedType.ANIME,
                kind = null,
            )
        } returns Result.success(Unit)

        actor.execute(
            AnimeDetailsNamespace.Command.Base(
                BaseDetailsCommand.ToggleFavourite(
                    targetId = 7L,
                    type = FavouriteLinkedType.ANIME,
                ),
            ),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.Base)
            event as AnimeDetailsNamespace.Event.Base
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
                type = FavouriteLinkedType.ANIME,
                kind = null,
            )
        } returns Result.failure(error)

        actor.execute(
            AnimeDetailsNamespace.Command.Base(
                BaseDetailsCommand.ToggleFavourite(
                    targetId = 7L,
                    type = FavouriteLinkedType.ANIME,
                ),
            ),
        ).test {
            val event = awaitItem()
            assertTrue(event is AnimeDetailsNamespace.Event.Base)
            event as AnimeDetailsNamespace.Event.Base
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
            AnimeDetailsNamespace.Command.Base(
                BaseDetailsCommand.ToggleFavourite(
                    targetId = 7L,
                    type = FavouriteLinkedType.ANIME,
                    kind = FavouriteKind.COMMON,
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
            AnimeDetailsNamespace.Command.Base(BaseDetailsCommand.FetchFavourites(force = true)),
        ).test {
            awaitComplete()
        }
        coVerify(exactly = 1) { fetchFavouritesUseCase(userId = 42L, force = true) }
    }

    @Test
    fun `WHEN FetchFavourites invoked without session SHOULD not call use case`() = runTest {
        every { sessionManager.currentSession } returns null

        actor.execute(
            AnimeDetailsNamespace.Command.Base(BaseDetailsCommand.FetchFavourites()),
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
