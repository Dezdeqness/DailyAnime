package com.dezdeqness.di.modules

import android.content.Context
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dezdeqness.data.core.GraphqlOperationNameInterceptor
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
object NetworkModule {

    const val BASE_CLIENT = "base_okhttp"

    private const val TIMEOUT = 15L

    @Named("logging")
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    @Named("chucker")
    @Singleton
    @Provides
    fun provideChuckerInterceptor(context: Context): Interceptor = ChuckerInterceptor(context)

    @Named("graphql_operation_name")
    @Singleton
    @Provides
    fun provideGraphqlOperationNameInterceptor(): ApolloInterceptor = GraphqlOperationNameInterceptor()

    @Named(BASE_CLIENT)
    @Singleton
    @Provides
    fun provideBaseHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()
}
