package com.dezdeqness.di.modules

import android.content.Context
import androidx.room.Room
import com.dezdeqness.data.database.MIGRATION_1_2
import com.dezdeqness.data.database.ShikimoriDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): ShikimoriDatabase {
        return Room
            .databaseBuilder(
                context.applicationContext,
                ShikimoriDatabase::class.java,
                "shikimori"
            )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

}
