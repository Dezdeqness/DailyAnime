package com.dezdeqness.feature.personallist.data

import com.dezdeqness.contract.userrate.model.PersonalListFilterEntity
import com.dezdeqness.contract.userrate.model.Sort
import com.dezdeqness.contract.userrate.repository.PersonalListFilterRepository
import javax.inject.Inject

internal class PersonalListFilterRepositoryImpl @Inject constructor(
    private val personalListFilterManager: PersonalListFilterManager,
) : PersonalListFilterRepository {

    override suspend fun getFilter(): PersonalListFilterEntity {
        val sort = personalListFilterManager.getSort(defaultValue = Sort.NAME.sort)
        return PersonalListFilterEntity.createFilter(sort = sort)
    }

    override suspend fun setFilter(personalListFilterEntity: PersonalListFilterEntity) {
        personalListFilterManager.setSort(sort = personalListFilterEntity.sort.sort)
    }

    override suspend fun setSort(sort: String) {
        personalListFilterManager.setSort(sort = sort)
    }
}
