package com.dezdeqness.di.modules

import com.dezdeqness.data.mapper.ApiErrorMapper
import com.dezdeqness.domain.mapper.ErrorMapper
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindErrorMapper(apiErrorMapper: ApiErrorMapper): ErrorMapper

}
