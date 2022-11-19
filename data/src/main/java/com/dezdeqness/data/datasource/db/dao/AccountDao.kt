package com.dezdeqness.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dezdeqness.data.model.db.AccountLocal

@Dao
interface AccountDao {

    @Query("SELECT * FROM 'account'")
    fun getAccount(): AccountLocal?

    @Insert
    fun insertAccount(accountLocal: AccountLocal)

    @Query("DELETE FROM 'account'")
    fun deleteAccount()

}
