package com.dezdeqness.contract.userrate.repository

import com.dezdeqness.contract.userrate.model.PersonalListFilterEntity

interface PersonalListFilterRepository {

    suspend fun getFilter(): PersonalListFilterEntity

    suspend fun setFilter(personalListFilterEntity: PersonalListFilterEntity)

    suspend fun setSort(sort: String)
}
