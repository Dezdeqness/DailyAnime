package com.dezdeqness.feature.details.anime.presentation

import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.userrate.EditRateUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import money.vivid.elmslie.core.store.ElmStore
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeDetailsViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var store: ElmStore<AnimeDetailsNamespace.Event, AnimeDetailsNamespace.State, AnimeDetailsNamespace.Effect, AnimeDetailsNamespace.Command>

    private lateinit var viewModel: AnimeDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { store.states } returns MutableStateFlow(AnimeDetailsNamespace.State(id = ANIME_ID))
        every { store.effects } returns flowOf()

        viewModel = AnimeDetailsViewModel(store = store, animeId = ANIME_ID)
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onSharePressed SHOULD dispatch Base SharePressed`() {
        viewModel.onSharePressed()
        verify { store.accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed)) }
    }

    @Test
    fun `WHEN onRetryClicked SHOULD dispatch Base RetryClicked`() {
        viewModel.onRetryClicked()
        verify { store.accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked)) }
    }

    @Test
    fun `WHEN onEditRateClicked SHOULD dispatch EditRateClicked`() {
        viewModel.onEditRateClicked()
        verify { store.accept(AnimeDetailsNamespace.Event.EditRateClicked) }
    }

    @Test
    fun `WHEN onUserRateBottomDialogClosed SHOULD dispatch EditRateClosed`() {
        viewModel.onUserRateBottomDialogClosed()
        verify { store.accept(AnimeDetailsNamespace.Event.EditRateClosed) }
    }

    @Test
    fun `WHEN onUserRateChanged SHOULD dispatch SaveUserRate`() {
        val model = EditRateUiModel(
            rateId = 1L,
            status = "watching",
            episodes = 3L,
            score = 7f,
            comment = "",
        )
        viewModel.onUserRateChanged(model)
        verify { store.accept(AnimeDetailsNamespace.Event.SaveUserRate(model)) }
    }

    @Test
    fun `WHEN onStatsClicked SHOULD dispatch StatsClicked`() {
        viewModel.onStatsClicked()
        verify { store.accept(AnimeDetailsNamespace.Event.StatsClicked) }
    }

    @Test
    fun `WHEN onSimilarClicked SHOULD dispatch SimilarClicked`() {
        viewModel.onSimilarClicked()
        verify { store.accept(AnimeDetailsNamespace.Event.SimilarClicked) }
    }

    @Test
    fun `WHEN onChronologyClicked SHOULD dispatch ChronologyClicked`() {
        viewModel.onChronologyClicked()
        verify { store.accept(AnimeDetailsNamespace.Event.ChronologyClicked) }
    }

    @Test
    fun `WHEN onRelatedAnimeClicked SHOULD dispatch RelatedClicked`() {
        viewModel.onRelatedAnimeClicked(animeId = 99L)
        verify { store.accept(AnimeDetailsNamespace.Event.RelatedClicked(animeId = 99L)) }
    }

    @Test
    fun `WHEN onCharacterClicked SHOULD dispatch CharacterClicked`() {
        viewModel.onCharacterClicked(characterId = 11L)
        verify { store.accept(AnimeDetailsNamespace.Event.CharacterClicked(characterId = 11L)) }
    }

    @Test
    fun `WHEN onScreenshotClicked SHOULD dispatch ScreenshotClicked`() {
        viewModel.onScreenshotClicked(previewUrl = "url")
        verify { store.accept(AnimeDetailsNamespace.Event.ScreenshotClicked(previewUrl = "url")) }
    }

    @Test
    fun `WHEN onVideoClicked SHOULD dispatch VideoClicked`() {
        viewModel.onVideoClicked(url = "https://video")
        verify { store.accept(AnimeDetailsNamespace.Event.VideoClicked(url = "https://video")) }
    }

    private companion object {
        const val ANIME_ID = 1L
    }
}
