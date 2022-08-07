package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.FilterEntity

interface SearchFilterRepository {

    fun getFilterConfiguration(): List<FilterEntity>

}
