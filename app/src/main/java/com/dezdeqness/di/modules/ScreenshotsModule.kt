package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.feature.screenshotsviewer.ScreenshotsViewModel
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.feature.screenshotsviewer.store.ScreenshotsNamespace.State
import com.dezdeqness.feature.screenshotsviewer.store.screenshotReducer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore
import money.vivid.elmslie.core.store.NoOpActor

@Module
abstract class ScreenshotsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ScreenshotsViewModel::class)
    abstract fun bindScreenshotsViewModel(viewModel: ScreenshotsViewModel): ViewModel

    companion object {
        @Provides
        fun provideScreenshotStore(
        ): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = screenshotReducer,
                actor = NoOpActor(),
            )
    }
}
