package com.dezdeqness.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dezdeqness.data.datasource.db.dao.AccountDao
import com.dezdeqness.data.model.db.AccountLocal

@TypeConverters(StatusConverter::class, StatsConverter::class)
@Database(
    entities = [AccountLocal::class],
    version = 1,
)
abstract class AccountDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {

        private const val DB_NAME = "account"
        private const val LEGACY_DB_NAME = "shikimori"
        private const val LEGACY_TABLE = "account"

        @Suppress("ktlint:standard:max-line-length")
        private const val LEGACY_SELECT =
            "SELECT id, nickname, avatar, last_online, name, sex, anime_stats, anime_scores, anime_types FROM $LEGACY_TABLE"

        @Suppress("ktlint:standard:max-line-length")
        private const val LEGACY_INSERT =
            "INSERT OR REPLACE INTO $LEGACY_TABLE " +
                "(id, nickname, avatar, last_online, name, sex, anime_stats, anime_scores, anime_types) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

        fun build(context: Context): AccountDatabase = Room
            .databaseBuilder(context.applicationContext, AccountDatabase::class.java, DB_NAME)
            .addCallback(migrateFromLegacyCallback(context.applicationContext))
            .build()

        private fun migrateFromLegacyCallback(context: Context) = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                val legacyFile = context.getDatabasePath(LEGACY_DB_NAME)
                if (!legacyFile.exists()) return

                try {
                    SQLiteDatabase.openDatabase(
                        legacyFile.absolutePath,
                        null,
                        SQLiteDatabase.OPEN_READONLY,
                    ).use { legacy ->
                        legacy.rawQuery(LEGACY_SELECT, null).use { cursor ->
                            while (cursor.moveToNext()) {
                                db.execSQL(
                                    LEGACY_INSERT,
                                    arrayOf(
                                        cursor.getLong(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        cursor.getString(6),
                                        cursor.getString(7),
                                        cursor.getString(8),
                                    ),
                                )
                            }
                        }
                    }
                } catch (_: SQLiteException) {
                    // Legacy DB predates the account table — nothing to copy.
                }
            }
        }
    }
}
