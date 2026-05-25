package com.dezdeqness.feature.details.anime.presentation

import com.dezdeqness.feature.details.common.presentation.BaseDetailsActions
import com.dezdeqness.feature.userrate.EditRateUiModel

interface AnimeDetailsActions : BaseDetailsActions {
    fun onEditRateClicked()
    fun onUserRateChanged(userRate: EditRateUiModel)
    fun onUserRateBottomDialogClosed()

    fun onStatsClicked()
    fun onSimilarClicked()
    fun onChronologyClicked()
    fun onRelatedAnimeClicked(animeId: Long)
    fun onCharacterClicked(characterId: Long)
    fun onScreenshotClicked(previewUrl: String)
    fun onVideoClicked(url: String)
}
