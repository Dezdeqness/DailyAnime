package com.dezdeqness.contract.filter.repository

import com.dezdeqness.contract.filter.model.FilterEntity

interface SearchFilterRepository {

    fun getFilterConfiguration(): List<FilterEntity>
}
