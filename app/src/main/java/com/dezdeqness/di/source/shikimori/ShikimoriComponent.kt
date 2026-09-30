package com.dezdeqness.di.source.shikimori

import com.dezdeqness.di.modules.AccountModule
import com.dezdeqness.di.modules.FavouriteModule
import com.dezdeqness.di.source.FeatureSubcomponents
import com.dezdeqness.di.source.SourceComponent
import com.dezdeqness.di.source.SourceSharedModule
import com.dezdeqness.feature.auth.di.AuthModule
import com.dezdeqness.feature.userrate.di.UserRatesModule
import com.dezdeqness.foundation.di.SourceScope
import dagger.Subcomponent

@SourceScope
@Subcomponent(
    modules = [
        ShikimoriSourceModule::class,
        FeatureSubcomponents::class,
        SourceSharedModule::class,
        ShikimoriRemoteModule::class,
        AccountModule::class,
        FavouriteModule::class,
        AuthModule::class,
        UserRatesModule::class,
    ],
)
interface ShikimoriComponent : SourceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ShikimoriComponent
    }
}
