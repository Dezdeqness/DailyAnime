package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.screenshotsviewer.ScreenshotsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ScreenshotsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ScreenshotsViewModel::class)
    abstract fun bindScreenshotsViewModel(viewModel: ScreenshotsViewModel): ViewModel

}
