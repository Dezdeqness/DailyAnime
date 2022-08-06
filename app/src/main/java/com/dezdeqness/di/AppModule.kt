package com.dezdeqness.di

import android.content.Context
import android.content.res.AssetManager
import com.dezdeqness.data.mapper.FilterMapper
import com.dezdeqness.data.mapper.GenreMapper
import com.dezdeqness.data.model.FilterTypeAdapter
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.ResourceProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAssetManager(context: Context) = context.assets

    @Singleton
    @Provides
    fun provideConfigurationProvider(
        assetManager: AssetManager,
        genreMapper: GenreMapper,
        filterMapper: FilterMapper,
        moshi: Moshi,
    ) =
        ConfigurationProvider(
            assetManager = assetManager,
            genreMapper = genreMapper,
            filterMapper = filterMapper,
            moshi = moshi,
        )

    @Singleton
    @Provides
    fun provideResourceProvider(context: Context) =
        ResourceProvider(context = context)

    @Singleton
    @Provides
    fun provideMoshi() =
        Moshi.Builder()
            .add(FilterTypeAdapter())
            .build()

}
