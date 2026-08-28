package com.dezdeqness.feature.screenshotsviewer.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.feature.screenshotsviewer.ScreenshotsViewModel
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.State
import com.dezdeqness.feature.screenshotsviewer.store.screenshotReducer
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.NoOpActor

@Module
abstract class ScreenshotsModule {

    companion object {
        @Provides
        internal fun provideScreenshotStore(configManager: ConfigManager): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = screenshotReducer(configManager.baseUrl.trimEnd('/')),
                actor = NoOpActor(),
            )
    }

    @Binds
    @IntoMap
    @ViewModelKey(ScreenshotsViewModel::class)
    internal abstract fun bindScreenshotsViewModel(viewModel: ScreenshotsViewModel): ViewModel
}
