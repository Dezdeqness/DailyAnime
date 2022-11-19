package com.dezdeqness.di.modules

import com.dezdeqness.data.AccountApiService
import com.dezdeqness.data.AuthorizationApiService
import com.dezdeqness.data.BuildConfig
import com.dezdeqness.data.core.RefreshTokenInterceptor
import com.dezdeqness.domain.usecases.RefreshTokenUseCase
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RemoteModule {

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    @Singleton
    @Provides
    fun providesHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Named("Authorization")
    @Singleton
    @Provides
    fun providesAuthorizationRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_AUTHORIZATION_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Named("Account")
    @Singleton
    @Provides
    fun providesAccoutRetrofit(@Named("okhttp_refresh") okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Named("okhttp_refresh")
    @Singleton
    @Provides
    fun providesHttpClientWithRefreshToken(
        interceptorRefresh: RefreshTokenInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptorRefresh)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideRefreshTokenInterceptor(refreshTokenUseCase: Lazy<RefreshTokenUseCase>): RefreshTokenInterceptor =
        RefreshTokenInterceptor(refreshTokenUseCase = refreshTokenUseCase)

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
