package com.dezdeqness.di.subcomponents

import androidx.lifecycle.ViewModel
import com.dezdeqness.di.ViewModelKey
import com.dezdeqness.presentation.features.routing.RoutingActivity
import com.dezdeqness.presentation.features.routing.RoutingViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [RoutingModule::class])
interface RoutingComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RoutingComponent
    }

    fun inject(activity: RoutingActivity)

}

@Module
abstract class RoutingModule {

    @Binds
    @IntoMap
    @ViewModelKey(RoutingViewModel::class)
    abstract fun bindRoutingViewModel(viewModel: RoutingViewModel): ViewModel

}
