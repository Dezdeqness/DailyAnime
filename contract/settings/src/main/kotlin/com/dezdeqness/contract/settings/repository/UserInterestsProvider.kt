package com.dezdeqness.contract.settings.repository

import com.dezdeqness.contract.anime.model.GenreEntity
import kotlinx.coroutines.flow.Flow

interface UserInterestsProvider {

    suspend fun getInterests(): List<GenreEntity>

    suspend fun getInterestIds(): List<String>

    fun observeInterests(): Flow<List<GenreEntity>>
}
