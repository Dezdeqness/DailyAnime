package com.dezdeqness.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dezdeqness.data.model.db.PinnedUserRate

@Dao
interface PinnedUserRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pinnedUserRate: PinnedUserRate)

    @Query("DELETE FROM pinned_user_rate WHERE userRateId = :userRateId")
    suspend fun deleteByUserRateId(userRateId: Long)

    @Query("SELECT COUNT(*) FROM pinned_user_rate WHERE userRateId = :userRateId")
    suspend fun isPinned(userRateId: Long): Int
}
