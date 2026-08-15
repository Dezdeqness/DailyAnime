package com.dezdeqness.contract.home.repository

import com.dezdeqness.contract.home.model.HomeEntity

interface HomeRepository {

    suspend fun getHomeSections(genreIds: List<String>): Result<HomeEntity>
}
