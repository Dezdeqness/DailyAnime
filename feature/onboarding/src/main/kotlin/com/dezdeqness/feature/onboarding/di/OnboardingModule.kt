package com.dezdeqness.feature.onboarding.di

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingViewModel
import com.dezdeqness.feature.onboarding.flow.presentation.notifications.NotificationsViewModel
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresViewModel
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OnboardingModule {

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel::class)
    internal abstract fun bindOnboardingViewModel(viewModel: OnboardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectGenresViewModel::class)
    internal abstract fun bindSelectGenresViewModel(viewModel: SelectGenresViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    internal abstract fun bindNotificationsViewModel(viewModel: NotificationsViewModel): ViewModel
}
