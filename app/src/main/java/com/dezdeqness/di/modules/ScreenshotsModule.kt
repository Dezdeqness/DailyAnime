package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.di.subcomponents.ScreenshotsArgsModule
import money.vivid.elmslie.core.store.NoOpActor
import com.dezdeqness.presentation.features.screenshotsviewer.ScreenshotsViewModel
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State
import com.dezdeqness.presentation.features.screenshotsviewer.store.screenshotReducer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Named

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
        ): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(
                    screenshotsList = screenshots,
                    index = index,
                ),
                reducer = screenshotReducer,
                actor = NoOpActor(),
            )
    }
}
