package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.modules.UserRateModule
import com.dezdeqness.presentation.features.userrate.UserRateActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [UserRateModule::class])
interface UserRateComponent {

    @Subcomponent.Builder
    interface Builder {

        fun userRateModule(module: com.dezdeqness.di.subcomponents.UserRateModule): Builder

        fun build(): UserRateComponent
    }

    fun inject(activity: UserRateActivity)
}

@Module
class UserRateModule(
    private val rateId: Long,
    private val title: String,
    private val overallEpisode: Int,
) {
    @Named("rateId")
    @Provides
    fun provideRateId() = rateId

    @Named("title")
    @Provides
    fun provideTitle() = title

    @Named("overallEpisode")
    @Provides
    fun provideOverallEpisode() = overallEpisode
}
