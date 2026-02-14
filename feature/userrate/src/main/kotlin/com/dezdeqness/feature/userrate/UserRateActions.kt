package com.dezdeqness.feature.userrate

@Suppress("TooManyFunctions")
interface UserRateActions {
    fun onStatusChanged(id: String)
    fun onScoreChanged(score: Long)
    fun onCommentChanged(comment: String)
    fun onSelectStatusClicked()
    fun onCloseSelectStatusClicked()
    fun onResetClicked()
    fun onChangeRateClicked()
    fun onBackPressed()
    fun onIncrementEpisode()
    fun onDecrementEpisode()
}
