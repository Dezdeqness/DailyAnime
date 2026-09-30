package com.dezdeqness.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.dezdeqness.data.TokenEntityProto
import com.dezdeqness.data.serializer.TokenSerializer
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object TokenStorageModule {

    const val SHIKIMORI_TOKEN = "shikimori_token"

    @Named(SHIKIMORI_TOKEN)
    @Singleton
    @Provides
    fun provideShikimoriTokenStore(context: Context): DataStore<TokenEntityProto> = DataStoreFactory.create(
        serializer = TokenSerializer(context),
        produceFile = { context.dataStoreFile("token_preferences.pb") },
    )
}
