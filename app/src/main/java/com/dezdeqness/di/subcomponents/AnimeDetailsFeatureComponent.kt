package com.dezdeqness.di.subcomponents

import com.dezdeqness.di.core.BaseComponent
import com.dezdeqness.di.modules.AnimeDetailsFeatureModule
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import dagger.Subcomponent

@Subcomponent(modules = [AnimeDetailsFeatureModule::class])
interface AnimeDetailsFeatureComponent : BaseComponent {

    fun messageConsumer(): MessageConsumer
    fun messageProvider(): BaseMessageProvider
    fun coroutineDispatcherProvider(): CoroutineDispatcherProvider

    @Subcomponent.Factory
    interface Factory {
        fun create(): AnimeDetailsFeatureComponent
    }
}
