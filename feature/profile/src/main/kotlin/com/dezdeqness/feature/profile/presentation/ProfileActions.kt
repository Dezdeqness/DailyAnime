package com.dezdeqness.feature.profile.presentation

interface ProfileActions {
    fun onSettingIconClicked()
    fun onStatsIconClicked()
    fun onHistoryIconClicked()
    fun onAchievementsClicked(userId: Long)
    fun onLoginClicked()
    fun onLogoutClicked()
    fun onRegistrationClicked()
    fun onFavouriteClicked(userId: Long)
}
