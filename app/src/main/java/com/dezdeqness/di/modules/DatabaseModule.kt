package com.dezdeqness.di.modules

import android.content.Context
import androidx.room.Room
import com.dezdeqness.data.database.ShikimoriDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): ShikimoriDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShikimoriDatabase::class.java,
            "shikimori"
        ).build()
    }

}
