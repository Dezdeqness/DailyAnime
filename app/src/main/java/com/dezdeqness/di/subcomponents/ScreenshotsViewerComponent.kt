package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.ScreenshotsModule
import com.dezdeqness.presentation.features.screenshotsviewer.ScreenshotsViewerActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [ScreenshotsArgsModule::class, ScreenshotsModule::class])
interface ScreenshotsViewerComponent {

    @Subcomponent.Builder
    interface Builder {
        fun argsModule(module: ScreenshotsArgsModule): Builder
        fun build(): ScreenshotsViewerComponent
    }

    fun inject(activity: ScreenshotsViewerActivity)
}

@Module
class ScreenshotsArgsModule(
    private val index: Int,
    private val screenshots: List<String>,
) {

    @Named("index")
    @Provides
    fun provideIndex() = index

    @Named("screenshots")
    @Provides
    fun provideScreenshots() = screenshots

}
