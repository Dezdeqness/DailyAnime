package com.dezdeqness.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dezdeqness.data.model.db.AccountSessionLocal

@Dao
interface AccountSessionDao {

    @Query("SELECT * FROM accounts WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveAccount(): AccountSessionLocal?

    @Query("UPDATE accounts SET is_active = 0")
    suspend fun deactivateAll()

    @Query("UPDATE accounts SET is_active = 0 WHERE id = :id")
    suspend fun deactivateAccount(id: String)

    @Query("UPDATE accounts SET is_active = 1 WHERE id = :id")
    suspend fun activateAccount(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountSessionLocal)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccount(id: String)

    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountSessionLocal>
}
