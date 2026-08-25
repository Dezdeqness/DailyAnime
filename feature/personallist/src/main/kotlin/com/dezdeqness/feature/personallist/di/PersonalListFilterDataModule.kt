package com.dezdeqness.feature.personallist.di

import com.dezdeqness.contract.userrate.repository.PersonalListFilterRepository
import com.dezdeqness.feature.personallist.data.PersonalListFilterRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PersonalListFilterDataModule {

    @Binds
    internal abstract fun bindPersonalListFilterRepository(
        personalListFilterRepository: PersonalListFilterRepositoryImpl,
    ): PersonalListFilterRepository
}
