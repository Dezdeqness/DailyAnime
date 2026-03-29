package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.feature.topicdetails.presentation.TopicDetailsViewModel
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsActor
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace.Command
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace.Effect
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace.Event
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace.State
import com.dezdeqness.feature.topicdetails.presentation.store.topicDetailsReducer
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.di.AssistedViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [AnimeModule::class, TopicModule::class])
abstract class TopicDetailsModule {

    companion object {

        @Provides
        fun provideTopicDetailsStore(actor: TopicDetailsActor): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = topicDetailsReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @AssistedViewModelKey(TopicDetailsViewModel::class)
    abstract fun bindTopicDetailsViewModelFactory(factory: TopicDetailsViewModel.Factory): AssistedViewModelFactory<out ViewModel>
}
