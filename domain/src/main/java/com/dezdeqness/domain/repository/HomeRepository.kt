package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.HomeEntity

interface HomeRepository {

    suspend fun getHomeSections(genreIds: List<String>): Result<HomeEntity>

}
