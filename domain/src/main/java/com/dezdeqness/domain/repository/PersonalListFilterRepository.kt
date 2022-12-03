package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.PersonalListFilterEntity

interface PersonalListFilterRepository {

    suspend fun getFilter(): PersonalListFilterEntity

    suspend fun setFilter(personalListFilterEntity: PersonalListFilterEntity)

    suspend fun setOrder(isAscending: Boolean)

    suspend fun setSort(sort: String)

}
