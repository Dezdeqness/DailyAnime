package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.domain.usecases.GetTopicUseCase
import com.dezdeqness.feature.news.presentation.NewsViewModel
import com.dezdeqness.feature.news.presentation.store.NewsActor
import com.dezdeqness.feature.news.presentation.store.NewsNamespace.Command
import com.dezdeqness.feature.news.presentation.store.NewsNamespace.Effect
import com.dezdeqness.feature.news.presentation.store.NewsNamespace.Event
import com.dezdeqness.feature.news.presentation.store.NewsNamespace.State
import com.dezdeqness.feature.news.presentation.store.newsReducer
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore

@Module(includes = [TopicModule::class])
abstract class NewsModule {

    companion object {

        @Provides
        fun provideGetTopicUseCase(topicRepository: TopicRepository) =
            GetTopicUseCase(topicRepository = topicRepository)

        @Provides
        fun provideNewsStore(actor: NewsActor): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = newsReducer,
                actor = actor,
            )
    }

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel
}
