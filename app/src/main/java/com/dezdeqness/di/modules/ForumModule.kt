package com.dezdeqness.di.modules

import androidx.lifecycle.ViewModel
import com.dezdeqness.contract.forum.repository.ForumRepository
import com.dezdeqness.data.ForumApiService
import com.dezdeqness.data.datasource.ForumRemoteDataSource
import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.data.datasource.ForumRemoteDataSourceImpl
import com.dezdeqness.data.repository.ForumRepositoryImpl
import com.dezdeqness.domain.usecases.GetForumsUseCase
import com.dezdeqness.domain.usecases.GetHotTopicsUseCase
import com.dezdeqness.feature.forum.presentation.ForumViewModel
import com.dezdeqness.feature.forum.presentation.store.ForumActor
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.Command
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.Effect
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.Event
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace.State
import com.dezdeqness.feature.forum.presentation.store.forumReducer
import com.dezdeqness.foundation.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import money.vivid.elmslie.core.store.ElmStore
import retrofit2.Retrofit

@Module(includes = [TopicModule::class])
abstract class ForumModule {

    companion object {

        @Provides
        fun provideForumApiService(retrofit: Retrofit): ForumApiService =
            retrofit.create(ForumApiService::class.java)

        @Provides
        fun provideGetForumsUseCase(forumRepository: ForumRepository) =
            GetForumsUseCase(forumRepository = forumRepository)

        @Provides
        fun provideGetHotTopicsUseCase(topicRepository: TopicRepository) =
            GetHotTopicsUseCase(topicRepository = topicRepository)

        @Provides
        fun provideForumStore(actor: ForumActor): ElmStore<Event, State, Effect, Command> =
            ElmStore(
                initialState = State(),
                reducer = forumReducer,
                actor = actor,
            )
    }

    @Binds
    abstract fun bindForumRemoteDataSource(
        impl: ForumRemoteDataSourceImpl,
    ): ForumRemoteDataSource

    @Binds
    abstract fun bindForumRepository(
        impl: ForumRepositoryImpl,
    ): ForumRepository

    @Binds
    @IntoMap
    @ViewModelKey(ForumViewModel::class)
    abstract fun bindForumViewModel(viewModel: ForumViewModel): ViewModel
}
