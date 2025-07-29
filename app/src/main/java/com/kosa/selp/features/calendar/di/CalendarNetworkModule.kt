package com.kosa.selp.features.calendar.di

import com.kosa.selp.features.calendar.data.remote.CalendarApiService
import com.kosa.selp.features.calendar.data.repositoryImpl.CalendarRepositoryImpl
import com.kosa.selp.features.calendar.domain.repository.CalendarRepository
import com.kosa.selp.features.calendar.domain.usecase.GetAllEventListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarNetworkModule {

    @Provides
    @Singleton
    fun provideCalendarApiService(retrofit: Retrofit): CalendarApiService {
        return retrofit.create(CalendarApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCalendarRepository(
        apiService: CalendarApiService
    ): CalendarRepository {
        return CalendarRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetAllEventListUseCase(
        repository: CalendarRepository
    ): GetAllEventListUseCase {
        return GetAllEventListUseCase(repository)
    }
}
