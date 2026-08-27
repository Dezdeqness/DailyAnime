package com.dezdeqness.feature.auth.di

import android.content.Context
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.feature.auth.data.AccountSessionDao
import com.dezdeqness.feature.auth.data.AuthDatabase
import com.dezdeqness.feature.auth.data.SessionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AuthDataModule {

    companion object {

        @Singleton
        @Provides
        internal fun provideAuthDatabase(context: Context): AuthDatabase =
            AuthDatabase.build(context)

        @Provides
        internal fun provideAccountSessionDao(database: AuthDatabase): AccountSessionDao =
            database.accountSessionDao()
    }

    @Binds
    @Singleton
    internal abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager
}
