package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.HomeEntity

interface HomeRepository {

    suspend fun getHomeSections(): Result<HomeEntity>

}
