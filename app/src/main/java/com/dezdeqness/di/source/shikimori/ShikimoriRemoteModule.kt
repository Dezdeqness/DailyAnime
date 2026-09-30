package com.dezdeqness.di.source.shikimori

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.network.okHttpClient
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.data.AccountApiService
import com.dezdeqness.data.AuthorizationApiService
import com.dezdeqness.data.core.AuthorizationTokenInterceptor
import com.dezdeqness.data.core.GraphqlAuthorizationInterceptor
import com.dezdeqness.data.core.RefreshTokenInterceptor
import com.dezdeqness.data.core.UserAgentTokenInterceptor
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.di.modules.NetworkModule
import com.dezdeqness.foundation.di.SourceScope
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Named
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
object ShikimoriRemoteModule {

    @Named("user_agent")
    @SourceScope
    @Provides
    fun provideUserAgentTokenInterceptor(): Interceptor = UserAgentTokenInterceptor()

    @Named("authorization")
    @SourceScope
    @Provides
    fun provideAuthorizationTokenInterceptor(tokenManager: TokenManager): Interceptor =
        AuthorizationTokenInterceptor(tokenManager = tokenManager)

    @Named("graphql_authorization")
    @SourceScope
    @Provides
    fun provideGraphqlAuthorizationInterceptor(tokenManager: TokenManager): Interceptor =
        GraphqlAuthorizationInterceptor(tokenManager = tokenManager)

    @Named("refresh")
    @SourceScope
    @Provides
    fun provideRefreshTokenInterceptor(sessionManager: Lazy<SessionManager>): Interceptor =
        RefreshTokenInterceptor(sessionManager = sessionManager)

    @SourceScope
    @Provides
    fun provideHttpClient(
        @Named(NetworkModule.BASE_CLIENT) baseClient: OkHttpClient,
        @Named("user_agent") userAgentTokenInterceptor: Interceptor,
        @Named("logging") loggingInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient = baseClient.newBuilder()
        .addInterceptor(userAgentTokenInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(chuckerInterceptor)
        .build()

    @Named("okhttp_refresh")
    @SourceScope
    @Provides
    fun provideHttpClientWithRefreshToken(
        @Named(NetworkModule.BASE_CLIENT) baseClient: OkHttpClient,
        @Named("authorization") authorizationInterceptor: Interceptor,
        @Named("user_agent") userAgentTokenInterceptor: Interceptor,
        @Named("refresh") refreshInterceptor: Interceptor,
        @Named("logging") loggingInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient = baseClient.newBuilder()
        .addInterceptor(authorizationInterceptor)
        .addInterceptor(userAgentTokenInterceptor)
        .addInterceptor(refreshInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(chuckerInterceptor)
        .build()

    @Named("shikimori_graphql_okhttp")
    @SourceScope
    @Provides
    fun provideGraphqlHttpClient(
        @Named(NetworkModule.BASE_CLIENT) baseClient: OkHttpClient,
        @Named("graphql_authorization") graphqlAuthorizationInterceptor: Interceptor,
        @Named("user_agent") userAgentTokenInterceptor: Interceptor,
        @Named("refresh") refreshInterceptor: Interceptor,
        @Named("logging") loggingInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient = baseClient.newBuilder()
        .addInterceptor(graphqlAuthorizationInterceptor)
        .addInterceptor(userAgentTokenInterceptor)
        .addInterceptor(refreshInterceptor)
        .addInterceptor(chuckerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Named("shikimori_graphql_client")
    @SourceScope
    @Provides
    fun provideGraphqlClient(
        @Named("graphql_operation_name") nameInterceptor: ApolloInterceptor,
        @Named("shikimori_graphql_okhttp") okHttpClient: OkHttpClient,
        configManager: ConfigManager,
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(configManager.baseGraphqlUrl)
        .addInterceptor(nameInterceptor)
        .okHttpClient(okHttpClient)
        .build()

    @SourceScope
    @Provides
    fun provideRetrofit(
        @Named("okhttp_refresh") okHttpClient: OkHttpClient,
        moshi: Moshi,
        configManager: ConfigManager,
    ): Retrofit = retrofit(configManager.baseUrl + "api/", okHttpClient, moshi)

    @Named("Authorization")
    @SourceScope
    @Provides
    fun provideAuthorizationRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        configManager: ConfigManager,
    ): Retrofit = retrofit(configManager.baseUrl, okHttpClient, moshi)

    @Named("Account")
    @SourceScope
    @Provides
    fun provideAccountRetrofit(
        @Named("okhttp_refresh") okHttpClient: OkHttpClient,
        moshi: Moshi,
        configManager: ConfigManager,
    ): Retrofit = retrofit(configManager.baseUrl + "api/", okHttpClient, moshi)

    @SourceScope
    @Provides
    fun provideAccountApiService(@Named("Account") retrofit: Retrofit): AccountApiService =
        retrofit.create(AccountApiService::class.java)

    @SourceScope
    @Provides
    fun provideAuthorizationApiService(@Named("Authorization") retrofit: Retrofit): AuthorizationApiService =
        retrofit.create(AuthorizationApiService::class.java)

    private fun retrofit(baseUrl: String, okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .build()
}
