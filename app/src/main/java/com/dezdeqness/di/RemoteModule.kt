package com.dezdeqness.di

import com.dezdeqness.data.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
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

    companion object {
        private const val TIMEOUT = 15L
    }

}
