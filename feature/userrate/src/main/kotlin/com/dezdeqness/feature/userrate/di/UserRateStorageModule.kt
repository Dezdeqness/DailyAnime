package com.dezdeqness.feature.userrate.di

import android.content.Context
import com.dezdeqness.feature.userrate.data.database.UserRateDatabase
import com.dezdeqness.feature.userrate.data.database.UserRatesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UserRateStorageModule {

    @Singleton
    @Provides
    internal fun provideUserRateDatabase(context: Context): UserRateDatabase = UserRateDatabase.build(context)

    @Provides
    internal fun provideUserRatesDao(database: UserRateDatabase): UserRatesDao = database.userRatesDao()
}
