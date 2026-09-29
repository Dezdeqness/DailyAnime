package com.dezdeqness.di.source.shikimori

import com.dezdeqness.di.source.SourceComponent
import com.dezdeqness.foundation.di.ShikimoriScope
import dagger.Subcomponent

@ShikimoriScope
@Subcomponent(modules = [ShikimoriSourceModule::class])
interface ShikimoriComponent : SourceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ShikimoriComponent
    }
}
