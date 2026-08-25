package com.dezdeqness.feature.forum.di

import com.dezdeqness.contract.forum.repository.ForumRepository
import com.dezdeqness.feature.forum.data.ForumApiService
import com.dezdeqness.feature.forum.data.ForumRemoteDataSource
import com.dezdeqness.feature.forum.data.ForumRemoteDataSourceImpl
import com.dezdeqness.feature.forum.data.ForumRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class ForumDataModule {

    companion object {
        @Provides
        internal fun provideForumApiService(retrofit: Retrofit): ForumApiService =
            retrofit.create(ForumApiService::class.java)
    }

    @Binds
    internal abstract fun bindForumRemoteDataSource(impl: ForumRemoteDataSourceImpl): ForumRemoteDataSource

    @Binds
    internal abstract fun bindForumRepository(impl: ForumRepositoryImpl): ForumRepository
}
