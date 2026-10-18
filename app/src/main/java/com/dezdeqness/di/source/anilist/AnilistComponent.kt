package com.dezdeqness.di.source.anilist

import com.dezdeqness.data.anilist.anime.di.AnilistAnimeDataModule
import com.dezdeqness.data.anilist.remote.di.AnilistRemoteModule
import com.dezdeqness.di.source.AnimeDetailsSourceComponent
import com.dezdeqness.di.source.AnimeStatsSourceComponent
import com.dezdeqness.di.source.SearchSourceComponent
import com.dezdeqness.di.source.unsupported.UnsupportedAccountModule
import com.dezdeqness.foundation.di.SourceScope
import dagger.Subcomponent

@SourceScope
@Subcomponent(
    modules = [
        AnilistSourceModule::class,
        AnilistRemoteModule::class,
        AnilistAnimeDataModule::class,
        UnsupportedAccountModule::class,
    ],
)
interface AnilistComponent :
    SearchSourceComponent,
    AnimeDetailsSourceComponent,
    AnimeStatsSourceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AnilistComponent
    }
}
