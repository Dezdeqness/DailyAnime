package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.debugscreen.DebugScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DebugModule {
    @Binds
    @IntoMap
    @ViewModelKey(DebugScreenViewModel::class)
    abstract fun bindDebugScreenViewModel(viewModel: DebugScreenViewModel): ViewModel
}
