package com.dezdeqness.di.modules

import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.data.TopicApiService
import com.dezdeqness.data.datasource.TopicRemoteDataSource
import com.dezdeqness.data.datasource.TopicRemoteDataSourceImpl
import com.dezdeqness.data.repository.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class TopicModule {

    companion object {

        @Provides
        fun provideTopicApiService(retrofit: Retrofit): TopicApiService = retrofit.create(TopicApiService::class.java)
    }

    @Binds
    abstract fun bindTopicRemoteDataSource(impl: TopicRemoteDataSourceImpl): TopicRemoteDataSource

    @Binds
    abstract fun bindTopicRepository(impl: TopicRepositoryImpl): TopicRepository
}
