package com.dezdeqness.feature.topicdetails.presentation

interface TopicDetailsActions {
    fun onBackPressed()
    fun onPullDownRefreshed()
    fun onRelatedAnimeClicked(animeId: Long)
    fun onVideoClicked(url: String)
}
