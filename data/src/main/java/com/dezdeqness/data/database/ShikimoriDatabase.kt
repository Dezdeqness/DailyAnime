package com.dezdeqness.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dezdeqness.data.datasource.db.dao.AccountDao
import com.dezdeqness.data.datasource.db.dao.PinnedUserRateDao
import com.dezdeqness.data.datasource.db.dao.UserRatesDao
import com.dezdeqness.data.model.db.AccountLocal
import com.dezdeqness.data.model.db.PinnedUserRate
import com.dezdeqness.data.model.db.UserRateLocal

@TypeConverters(StatusConverter::class, StatsConverter::class)
@Database(
    entities = [
        AccountLocal::class,
        UserRateLocal::class,
        PinnedUserRate::class
    ],
    version = 2
)
abstract class ShikimoriDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun userRatesDao(): UserRatesDao
    abstract fun pinnedUserRateDao(): PinnedUserRateDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `pinned_user_rate` (
                `userRateId` INTEGER NOT NULL,
                `groupId` TEXT NOT NULL,
                PRIMARY KEY(`userRateId`)
            )
        """.trimIndent())
    }
}
