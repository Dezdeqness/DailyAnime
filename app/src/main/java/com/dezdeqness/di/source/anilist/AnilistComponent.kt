package com.dezdeqness.di.source.anilist

import com.dezdeqness.data.anilist.anime.di.AnilistAnimeDataModule
import com.dezdeqness.data.anilist.remote.di.AnilistRemoteModule
import com.dezdeqness.di.source.SearchSourceComponent
import com.dezdeqness.foundation.di.SourceScope
import dagger.Subcomponent

@SourceScope
@Subcomponent(
    modules = [
        AnilistSourceModule::class,
        AnilistRemoteModule::class,
        AnilistAnimeDataModule::class,
    ],
)
interface AnilistComponent : SearchSourceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AnilistComponent
    }
}
