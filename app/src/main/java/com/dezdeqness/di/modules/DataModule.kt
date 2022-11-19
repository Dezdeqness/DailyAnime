package com.dezdeqness.di.modules

import com.dezdeqness.data.mapper.ApiErrorMapper
import com.dezdeqness.domain.mapper.ErrorMapper
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindErrorMapper(apiErrorMapper: ApiErrorMapper): ErrorMapper

}
