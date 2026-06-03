package com.dezdeqness.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dezdeqness.data.datasource.db.dao.AccountDao
import com.dezdeqness.data.datasource.db.dao.AccountSessionDao
import com.dezdeqness.data.datasource.db.dao.UserRatesDao
import com.dezdeqness.data.model.db.AccountLocal
import com.dezdeqness.data.model.db.AccountSessionLocal
import com.dezdeqness.data.model.db.UserRateLocal

@TypeConverters(StatusConverter::class, StatsConverter::class)
@Database(
    entities = [AccountLocal::class, UserRateLocal::class, AccountSessionLocal::class],
    version = 2,
)
abstract class ShikimoriDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun accountSessionDao(): AccountSessionDao

    abstract fun userRatesDao(): UserRatesDao
}
