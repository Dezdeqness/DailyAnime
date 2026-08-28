package com.dezdeqness.feature.history.di

import com.dezdeqness.contract.history.repository.HistorySearchRepository
import com.dezdeqness.feature.history.data.HistorySearchRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class HistoryModule {

    @Binds
    internal abstract fun bindHistorySearchRepository(
        historySearchRepository: HistorySearchRepositoryImpl,
    ): HistorySearchRepository
}
