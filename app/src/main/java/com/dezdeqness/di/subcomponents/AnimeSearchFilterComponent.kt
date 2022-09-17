package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.AnimeSearchFilterModule
import com.dezdeqness.presentation.features.searchfilter.anime.AnimeSearchFilterBottomSheetDialog
import com.dezdeqness.presentation.models.AnimeSearchFilter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [AnimeSearchFilterModule::class])
interface AnimeSearchFilterComponent {

    @Subcomponent.Builder
    interface Builder {
        fun filterModule(module: SearchFilterListModule): Builder
        fun build(): AnimeSearchFilterComponent
    }

    fun inject(fragment: AnimeSearchFilterBottomSheetDialog)
}

@Module
class SearchFilterListModule(private val list: List<AnimeSearchFilter>) {
    @Named("searchFiltersList")
    @Provides
    fun provideAnimeSearchFilterList() = list
}
