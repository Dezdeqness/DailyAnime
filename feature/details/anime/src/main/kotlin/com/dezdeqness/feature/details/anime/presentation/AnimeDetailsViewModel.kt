package com.dezdeqness.feature.details.anime.presentation

import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.feature.details.anime.presentation.store.AnimeDetailsNamespace
import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsEvent
import com.dezdeqness.feature.userrate.EditRateUiModel
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

object AnimeIdKey : CreationExtras.Key<Long>

class AnimeDetailsViewModel(
    store: ElmStore<AnimeDetailsNamespace.Event, AnimeDetailsNamespace.State, AnimeDetailsNamespace.Effect, AnimeDetailsNamespace.Command>,
    private val animeId: Long,
) : BaseStoreViewModel<AnimeDetailsNamespace.Event, AnimeDetailsNamespace.State, AnimeDetailsNamespace.Effect, AnimeDetailsNamespace.Command>(
    store = store,
    initialState = AnimeDetailsNamespace.State(id = animeId),
    sharingStarted = SharingStarted.Lazily,
    initialEvent = AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.InitialLoad(animeId)),
) {

    fun onSharePressed() = accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.SharePressed))
    fun onRetryClicked() = accept(AnimeDetailsNamespace.Event.Base(BaseDetailsEvent.RetryClicked))
    fun onEditRateClicked() = accept(AnimeDetailsNamespace.Event.EditRateClicked)
    fun onUserRateBottomDialogClosed() = accept(AnimeDetailsNamespace.Event.EditRateClosed)
    fun onUserRateChanged(userRate: EditRateUiModel) =
        accept(AnimeDetailsNamespace.Event.SaveUserRate(userRate))
    fun onStatsClicked() = accept(AnimeDetailsNamespace.Event.StatsClicked)
    fun onSimilarClicked() = accept(AnimeDetailsNamespace.Event.SimilarClicked)
    fun onChronologyClicked() = accept(AnimeDetailsNamespace.Event.ChronologyClicked)
    fun onRelatedAnimeClicked(animeId: Long) =
        accept(AnimeDetailsNamespace.Event.RelatedClicked(animeId))
    fun onCharacterClicked(characterId: Long) =
        accept(AnimeDetailsNamespace.Event.CharacterClicked(characterId))
    fun onScreenshotClicked(previewUrl: String) =
        accept(AnimeDetailsNamespace.Event.ScreenshotClicked(previewUrl))
    fun onVideoClicked(url: String) =
        accept(AnimeDetailsNamespace.Event.VideoClicked(url))

    class Factory @Inject constructor(
        private val store: ElmStore<
                AnimeDetailsNamespace.Event,
                AnimeDetailsNamespace.State,
                AnimeDetailsNamespace.Effect,
                AnimeDetailsNamespace.Command>,
    ) : AssistedViewModelFactory<AnimeDetailsViewModel> {

        override fun create(extras: CreationExtras): AnimeDetailsViewModel {
            val animeId = extras[AnimeIdKey] ?: 0L
            return AnimeDetailsViewModel(store = store, animeId = animeId)
        }
    }
}
