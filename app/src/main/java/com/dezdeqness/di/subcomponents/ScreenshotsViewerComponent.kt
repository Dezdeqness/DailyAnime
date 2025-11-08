package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.ScreenshotsModule
import dagger.Subcomponent

@Subcomponent(modules = [ScreenshotsModule::class])
interface ScreenshotsViewerComponent : BaseComponent{

    @Subcomponent.Builder
    interface Builder {
        fun build(): ScreenshotsViewerComponent
    }
}
