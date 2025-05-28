package com.dezdeqness.di.modules

import com.dezdeqness.data.CalendarApiService
import com.dezdeqness.data.datasource.CalendarRemoteDataSource
import com.dezdeqness.data.datasource.CalendarRemoteDataSourceImpl
import com.dezdeqness.data.repository.CalendarRepositoryImpl
import com.dezdeqness.domain.repository.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class CalendarModule {

    companion object {
        @Provides
        fun provideCalendarApiService(retrofit: Retrofit): CalendarApiService =
            retrofit.create(CalendarApiService::class.java)
    }

    @Binds
    abstract fun bindCalendarRemoteDataSource(
        calendarRemoteDataSource: CalendarRemoteDataSourceImpl,
    ): CalendarRemoteDataSource

    @Binds
    abstract fun bindCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl,
        ): CalendarRepository

}
