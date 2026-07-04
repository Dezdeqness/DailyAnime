package com.dezdeqness.feature.details.related.presentation

interface RelatedListActions {
    fun onAnimeClicked(animeId: Long, title: String)
    fun onRetryClicked()
    fun onBackPressed()
}
