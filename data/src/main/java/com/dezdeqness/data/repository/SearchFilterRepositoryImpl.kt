package com.dezdeqness.data.repository

import com.dezdeqness.data.mapper.FilterMapper
import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.TypeEntity
import com.dezdeqness.domain.repository.SearchFilterRepository
import javax.inject.Inject

class SearchFilterRepositoryImpl @Inject constructor(
    private val configurationProvider: ConfigurationProvider,
    private val filterMapper: FilterMapper,
) : SearchFilterRepository {

    override fun getFilterConfiguration(): List<FilterEntity> {
        val filters = configurationProvider.getFilters()

        val genres = configurationProvider
            .getListGenre()
            .filter { it.type == TypeEntity.ANIME }
            .map { filterMapper.fromResponse(it) }
        return genres + filters
    }
}
