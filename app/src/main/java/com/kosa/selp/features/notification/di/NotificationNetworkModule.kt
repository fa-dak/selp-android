package com.kosa.selp.features.notification.di

import com.kosa.selp.features.notification.repository.NotificationRepository
import com.kosa.selp.features.notification.service.NotificationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        api: NotificationApi
    ): NotificationRepository {
        return NotificationRepository(api)
    }
}
