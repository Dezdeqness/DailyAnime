package com.dezdeqness.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dezdeqness.data.model.db.UserRateLocal

@Dao
interface UserRatesDao {

    @Query("SELECT * FROM 'user_rate'")
    fun getUserRates(): List<UserRateLocal>

    @Query("SELECT * FROM 'user_rate' WHERE status = :status")
    fun getUserRates(status: String): List<UserRateLocal>

    @Query("SELECT * FROM 'user_rate' WHERE id = :rateId")
    fun getUserRateByRateId(rateId: Long): UserRateLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserRates(list: List<UserRateLocal>)

    @Query("DELETE FROM 'user_rate' WHERE id = :rateId")
    fun deleteUserRateByRateId(rateId: Long)

    @Query("DELETE FROM 'user_rate'")
    fun deleteUserRates()

    @Query("DELETE FROM 'user_rate' WHERE status = :status")
    fun deleteUserRatesByStatus(status: String)

    @Query("UPDATE 'user_rate' SET score = :score, status = :status, episodes = :episodes, text = :text WHERE id = :rateId")
    fun updateUserRate(
        rateId: Int,
        score: Int,
        status: String,
        episodes: Int,
        text: String,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserRate(userRate: UserRateLocal)

}
