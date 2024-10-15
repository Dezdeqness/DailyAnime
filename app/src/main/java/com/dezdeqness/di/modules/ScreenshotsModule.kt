package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.di.subcomponents.ScreenshotsArgsModule
import com.dezdeqness.pod.core.scope.plantScope
import com.dezdeqness.pod.core.store.internal.PlantStore
import com.dezdeqness.presentation.features.screenshotsviewer.ScreenshotsViewModel
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State
import com.dezdeqness.presentation.features.screenshotsviewer.store.screenshotReducer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

private const val KEY = "ScreenshotStore"

@Module(includes = [ScreenshotsArgsModule::class])
abstract class ScreenshotsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ScreenshotsViewModel::class)
    abstract fun bindScreenshotsViewModel(viewModel: ScreenshotsViewModel): ViewModel

    companion object {
        @Provides
        fun provideScreenshotStore(
            @Named("index") index: Int,
            @Named("screenshots") screenshots: List<String>,
            coroutineDispatcherProvider: CoroutineDispatcherProvider,
            appLogger: AppLogger,
        ): PlantStore<Event, State, Effect> =
            PlantStore(
                state = State(
                    screenshotsList = screenshots,
                    index = index,
                ),
                reducer = screenshotReducer,
                storeScope = plantScope(
                    key = KEY,
                    dispatcher = coroutineDispatcherProvider.io(),
                    onError = { throwable ->
                        appLogger.logInfo(tag = KEY, throwable = throwable)
                    }
                )
            )
    }

}
