package com.dezdeqness.feature.calendar.di

import com.dezdeqness.contract.calendar.repository.CalendarRepository
import com.dezdeqness.feature.calendar.data.CalendarApiService
import com.dezdeqness.feature.calendar.data.CalendarRemoteDataSource
import com.dezdeqness.feature.calendar.data.CalendarRemoteDataSourceImpl
import com.dezdeqness.feature.calendar.data.CalendarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class CalendarDataModule {

    companion object {
        @Provides
        internal fun provideCalendarApiService(retrofit: Retrofit): CalendarApiService =
            retrofit.create(CalendarApiService::class.java)
    }

    @Binds
    internal abstract fun bindCalendarRemoteDataSource(
        calendarRemoteDataSource: CalendarRemoteDataSourceImpl,
    ): CalendarRemoteDataSource

    @Binds
    internal abstract fun bindCalendarRepository(calendarRepositoryImpl: CalendarRepositoryImpl): CalendarRepository
}
