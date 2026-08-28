package com.dezdeqness.feature.searchfilter.di

import com.dezdeqness.contract.filter.repository.SearchFilterRepository
import com.dezdeqness.feature.searchfilter.data.SearchFilterRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class SearchFilterModule {

    @Binds
    internal abstract fun bindSearchFilterRepository(
        searchFilterRepository: SearchFilterRepositoryImpl,
    ): SearchFilterRepository
}
