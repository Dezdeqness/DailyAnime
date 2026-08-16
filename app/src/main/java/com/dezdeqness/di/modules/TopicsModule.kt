package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.topic.usecases.GetTopicUseCase
import com.dezdeqness.domain.topic.usecases.GetTopicUseCaseImpl
import com.dezdeqness.feature.topics.presentation.TopicListViewModel
import com.dezdeqness.feature.topics.presentation.store.TopicListActor
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace.Command
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace.Effect
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace.Event
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace.State
import com.dezdeqness.feature.topics.presentation.store.topicListReducer
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [TopicModule::class])
abstract class TopicsModule {

    companion object {

        @Provides
        fun provideTopicListStore(actor: TopicListActor): ElmStore<Event, State, Effect, Command> = ElmStore(
            initialState = State(),
            reducer = topicListReducer,
            actor = actor,
        )
    }

    @Binds
    abstract fun bindGetTopicUseCase(getTopicUseCase: GetTopicUseCaseImpl): GetTopicUseCase

    @Binds
    @IntoMap
    @ViewModelKey(TopicListViewModel::class)
    abstract fun bindTopicListViewModel(viewModel: TopicListViewModel): ViewModel
}
