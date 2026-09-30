package com.dezdeqness.di.modules

import android.content.Context
import com.dezdeqness.data.database.AccountDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AccountStorageModule {

    @Singleton
    @Provides
    fun provideAccountDatabase(context: Context): AccountDatabase = AccountDatabase.build(context)

    @Provides
    fun provideAccountDao(database: AccountDatabase) = database.accountDao()
}
