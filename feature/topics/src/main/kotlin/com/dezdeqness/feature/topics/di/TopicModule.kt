package com.dezdeqness.feature.topics.di

import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.feature.topics.data.TopicApiService
import com.dezdeqness.feature.topics.data.TopicRemoteDataSource
import com.dezdeqness.feature.topics.data.TopicRemoteDataSourceImpl
import com.dezdeqness.feature.topics.data.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class TopicModule {

    companion object {
        @Provides
        internal fun provideTopicApiService(retrofit: Retrofit): TopicApiService =
            retrofit.create(TopicApiService::class.java)
    }

    @Binds
    internal abstract fun bindTopicRemoteDataSource(impl: TopicRemoteDataSourceImpl): TopicRemoteDataSource

    @Binds
    internal abstract fun bindTopicRepository(impl: TopicRepositoryImpl): TopicRepository
}
