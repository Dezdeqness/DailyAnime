package com.dezdeqness.feature.topicdetails.presentation.models

sealed interface BaseRelatedAnime {
    data object Initial : BaseRelatedAnime
    data object Loading : BaseRelatedAnime
    data object Error : BaseRelatedAnime
    data class Loaded(val anime: LinkedAnimeUiModel) : BaseRelatedAnime
}
