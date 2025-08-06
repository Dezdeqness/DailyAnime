package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.AchievementsModule
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named


@Subcomponent(modules = [AchievementsModule::class, AchievementsArgsModule::class])
interface AchievementsSubcomponent : BaseComponent {
    @Subcomponent.Builder
    interface Builder {

        fun argsModule(module: AchievementsArgsModule): Builder

        fun build(): AchievementsSubcomponent
    }
}

@Module
class AchievementsArgsModule(private val userId: Long) {

    @Named("userId")
    @Provides
    fun provideUserId() = userId

}
