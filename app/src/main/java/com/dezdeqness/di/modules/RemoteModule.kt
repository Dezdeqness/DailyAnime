package com.dezdeqness.di.modules

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.network.okHttpClient
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dezdeqness.data.AccountApiService
import com.dezdeqness.data.AuthorizationApiService
import com.dezdeqness.data.core.AuthorizationTokenInterceptor
import com.dezdeqness.data.core.GraphqlOperationNameInterceptor
import com.dezdeqness.data.core.RefreshTokenInterceptor
import com.dezdeqness.data.core.UserAgentTokenInterceptor
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.domain.usecases.RefreshTokenUseCase
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RemoteModule {

    @Named("logging")
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    @Named("chucker")
    @Singleton
    @Provides
    fun provideChuckerInterceptor(context: Context): Interceptor =
        ChuckerInterceptor(context)

    @Singleton
    @Provides
    fun providesHttpClient(
        @Named("user_agent") userAgentTokenInterceptor: Interceptor,
        @Named("logging") loggingInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(
        @Named("okhttp_refresh") okHttpClient: OkHttpClient,
        moshi: Moshi,
        configManager: ConfigManager,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(configManager.baseUrl + "api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Named("Authorization")
    @Singleton
    @Provides
    fun providesAuthorizationRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        configManager: ConfigManager,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(configManager.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Named("Account")
    @Singleton
    @Provides
    fun providesAccountRetrofit(
        @Named("okhttp_refresh") okHttpClient: OkHttpClient,
        moshi: Moshi,
        configManager: ConfigManager,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(configManager.baseUrl + "api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Named("okhttp_refresh")
    @Singleton
    @Provides
    fun providesHttpClientWithRefreshToken(
        @Named("authorization") authorizationInterceptor: Interceptor,
        @Named("user_agent") userAgentTokenInterceptor: Interceptor,
        @Named("refresh") refreshInterceptor: Interceptor,
        @Named("logging") loggingInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(userAgentTokenInterceptor)
            .addInterceptor(refreshInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()


    @Named("graphql")
    @Singleton
    @Provides
    fun providesGraphqlHttpClient(
        @Named("user_agent") userAgentTokenInterceptor: Interceptor,
        @Named("chucker") chuckerInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentTokenInterceptor)
            .addInterceptor(chuckerInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideGraphqlClient(
        @Named("graphql_operation_name") nameInterceptor: ApolloInterceptor,
        @Named("graphql") okHttpClient: OkHttpClient,
        configManager: ConfigManager,
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(configManager.baseGraphqlUrl)
        .addInterceptor(nameInterceptor)
        .okHttpClient(okHttpClient)
        .build()

    @Named("refresh")
    @Singleton
    @Provides
    fun provideRefreshTokenInterceptor(refreshTokenUseCase: Lazy<RefreshTokenUseCase>): Interceptor =
        RefreshTokenInterceptor(refreshTokenUseCase = refreshTokenUseCase)

    @Named("authorization")
    @Singleton
    @Provides
    fun provideAuthorizationTokenInterceptor(tokenManager: TokenManager): Interceptor =
        AuthorizationTokenInterceptor(tokenManager = tokenManager)

    @Named("graphql_operation_name")
    @Singleton
    @Provides
    fun provideGraphqlOperationNameInterceptor(): ApolloInterceptor =
        GraphqlOperationNameInterceptor()

    @Named("user_agent")
    @Singleton
    @Provides
    fun provideUserAgentTokenInterceptor(): Interceptor =
        UserAgentTokenInterceptor()

    @Singleton
    @Provides
    fun provideAccountApiService(@Named("Account") retrofit: Retrofit): AccountApiService =
        retrofit.create(AccountApiService::class.java)

    @Singleton
    @Provides
    fun provideAuthorizationApiService(
        @Named("Authorization") retrofit: Retrofit
    ): AuthorizationApiService =
        retrofit.create(AuthorizationApiService::class.java)

    companion object {
        const val TIMEOUT = 15L
    }

}
