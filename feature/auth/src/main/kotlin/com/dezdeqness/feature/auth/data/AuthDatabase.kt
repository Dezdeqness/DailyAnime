package com.dezdeqness.feature.auth.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [AccountSessionLocal::class],
    version = 1,
)
internal abstract class AuthDatabase : RoomDatabase() {

    abstract fun accountSessionDao(): AccountSessionDao

    companion object {

        private const val DB_NAME = "auth"
        private const val LEGACY_DB_NAME = "shikimori"
        private const val LEGACY_TABLE = "accounts"

        fun build(context: Context): AuthDatabase = Room
            .databaseBuilder(context.applicationContext, AuthDatabase::class.java, DB_NAME)
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
                        legacy.rawQuery(
                            "SELECT id, account_type, is_active FROM $LEGACY_TABLE",
                            null,
                        ).use { cursor ->
                            while (cursor.moveToNext()) {
                                db.execSQL(
                                    "INSERT OR REPLACE INTO $LEGACY_TABLE " +
                                        "(id, account_type, is_active) VALUES (?, ?, ?)",
                                    arrayOf(
                                        cursor.getString(0),
                                        cursor.getString(1),
                                        cursor.getInt(2),
                                    ),
                                )
                            }
                        }
                    }
                } catch (_: SQLiteException) {
                    // Legacy DB predates the accounts table — nothing to copy.
                }
            }
        }
    }
}
