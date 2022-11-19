package com.dezdeqness.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dezdeqness.data.datasource.db.dao.AccountDao
import com.dezdeqness.data.model.db.AccountLocal

@Database(entities = [AccountLocal::class], version = 1)
abstract class ShikimoriDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

}
