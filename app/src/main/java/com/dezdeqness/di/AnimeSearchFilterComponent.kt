package com.dezdeqness.di

import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog
import dagger.Subcomponent

@Subcomponent(modules = [AnimeSearchFilterModule::class])
interface AnimeSearchFilterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeSearchFilterComponent
    }

    fun inject(fragment: AnimeSearchFilterBottomSheetDialog)
}