package com.dezdeqness.data.repository

import com.dezdeqness.data.manager.PersonalListFilterManager
import com.dezdeqness.domain.model.PersonalListFilterEntity
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.domain.repository.PersonalListFilterRepository
import javax.inject.Inject

class PersonalListFilterRepositoryImpl @Inject constructor(
    private val personalListFilterManager: PersonalListFilterManager,
) : PersonalListFilterRepository {

    override suspend fun getFilter(): PersonalListFilterEntity {
        val sort = personalListFilterManager.getSort(defaultValue = Sort.NAME.sort)
        val isAscending = personalListFilterManager.getOrder(defaultValue = true)
        return PersonalListFilterEntity.createFilter(isAscending = isAscending, sort = sort)
    }

    override suspend fun setFilter(personalListFilterEntity: PersonalListFilterEntity) {
        personalListFilterManager.setOrder(isAscending = personalListFilterEntity.isAscending)
        personalListFilterManager.setSort(sort = personalListFilterEntity.sort.sort)
    }

    override suspend fun setOrder(isAscending: Boolean) {
        personalListFilterManager.setOrder(isAscending = isAscending)
    }

    override suspend fun setSort(sort: String) {
        personalListFilterManager.setSort(sort = sort)
    }

}
