package com.dezdeqness.feature.userrate.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserRateLocal::class],
    version = 1,
)
internal abstract class UserRateDatabase : RoomDatabase() {

    abstract fun userRatesDao(): UserRatesDao

    companion object {

        private const val DB_NAME = "user_rate"
        private const val LEGACY_DB_NAME = "shikimori"
        private const val LEGACY_TABLE = "user_rate"

        @Suppress("ktlint:standard:max-line-length")
        private const val LEGACY_SELECT =
            "SELECT id, score, status, text, episodes, chapters, volumes, text_html, rewatches, created_at, updated_at, " +
                "anime_id, name, russian, url, kind, anime_score, anime_status, anime_episodes, anime_episodes_aired, " +
                "aired_on, released_on, original, preview, x96, x48 FROM $LEGACY_TABLE"

        @Suppress("ktlint:standard:max-line-length")
        private const val LEGACY_INSERT =
            "INSERT OR REPLACE INTO $LEGACY_TABLE " +
                "(id, score, status, text, episodes, chapters, volumes, text_html, rewatches, created_at, updated_at, " +
                "anime_id, name, russian, url, kind, anime_score, anime_status, anime_episodes, anime_episodes_aired, " +
                "aired_on, released_on, original, preview, x96, x48) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

        fun build(context: Context): UserRateDatabase = Room
            .databaseBuilder(context.applicationContext, UserRateDatabase::class.java, DB_NAME)
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
                                        cursor.getLong(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getLong(4),
                                        cursor.getLong(5),
                                        cursor.getLong(6),
                                        cursor.getString(7),
                                        cursor.getLong(8),
                                        cursor.getLong(9),
                                        cursor.getLong(10),
                                        cursor.getLong(11),
                                        cursor.getString(12),
                                        cursor.getString(13),
                                        cursor.getString(14),
                                        cursor.getString(15),
                                        cursor.getFloat(16),
                                        cursor.getString(17),
                                        cursor.getInt(18),
                                        cursor.getInt(19),
                                        cursor.getLong(20),
                                        cursor.getLong(21),
                                        cursor.getString(22),
                                        cursor.getString(23),
                                        cursor.getString(24),
                                        cursor.getString(25),
                                    ),
                                )
                            }
                        }
                    }
                } catch (_: SQLiteException) {
                    // Legacy DB predates the user_rate table — nothing to copy.
                }
            }
        }
    }
}
