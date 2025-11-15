package com.dezdeqness.feature.onboarding.selectgenres.presentation

interface SelectGenresActions {
    fun onBackPressed()
    fun onGenreClicked(genreId: String)
    fun onSaveClicked()
}
