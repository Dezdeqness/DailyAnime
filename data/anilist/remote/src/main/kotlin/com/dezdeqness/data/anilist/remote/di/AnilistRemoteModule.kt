package com.dezdeqness.data.anilist.remote.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.network.okHttpClient
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.foundation.di.SourceScope
import dagger.Module
import dagger.Provides
import javax.inject.Named
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Module
object AnilistRemoteModule {

    const val GRAPHQL_CLIENT = "anilist_graphql_client"

    private const val GRAPHQL_OKHTTP = "anilist_graphql_okhttp"

    @Named(GRAPHQL_OKHTTP)
    @SourceScope
    @Provides
    fun provideGraphqlHttpClient(
        @Named("base_okhttp") baseClient: OkHttpClient,
        @Named("logging") loggingInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient = baseClient.newBuilder()
        .addInterceptor(chuckerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Named(GRAPHQL_CLIENT)
    @SourceScope
    @Provides
    fun provideGraphqlClient(
        @Named("graphql_operation_name") nameInterceptor: ApolloInterceptor,
        @Named(GRAPHQL_OKHTTP) okHttpClient: OkHttpClient,
        configManager: ConfigManager,
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(configManager.baseAnilistGraphqlUrl)
        .addInterceptor(nameInterceptor)
        .okHttpClient(okHttpClient)
        .build()
}
