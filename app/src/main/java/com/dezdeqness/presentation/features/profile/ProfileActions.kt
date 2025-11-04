package com.dezdeqness.presentation.features.profile

interface ProfileActions {
    fun onSettingIconClicked()
    fun onStatsIconClicked()
    fun onHistoryIconClicked()
    fun onAchievementsClicked(userId: Long)
    fun onLoginCLicked()
    fun onLogoutClicked()
    fun onRegistrationClicked()
    fun onFavouriteClicked(userId: Long)
}
