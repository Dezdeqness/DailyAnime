package com.dezdeqness.feature.auth.di

import android.content.Context
import com.dezdeqness.feature.auth.data.AccountSessionDao
import com.dezdeqness.feature.auth.data.AuthDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AuthStorageModule {

    @Singleton
    @Provides
    internal fun provideAuthDatabase(context: Context): AuthDatabase = AuthDatabase.build(context)

    @Provides
    internal fun provideAccountSessionDao(database: AuthDatabase): AccountSessionDao =
        database.accountSessionDao()
}
