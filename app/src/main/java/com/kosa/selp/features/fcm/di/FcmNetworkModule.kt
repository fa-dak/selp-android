package com.kosa.selp.features.fcm.di

import com.kosa.selp.features.fcm.data.remote.FcmApiService
import com.kosa.selp.features.fcm.data.repositoryImpl.FcmRepositoryImpl
import com.kosa.selp.features.fcm.domain.repository.FcmRepository
import com.kosa.selp.features.fcm.domain.usecase.RegisterFcmTokenUseCase
import com.kosa.selp.features.fcm.domain.usecase.RegisterNotificationUseCase
import com.kosa.selp.features.fcm.domain.usecase.SendNotificationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FcmNetworkModule {

    @Provides
    @Singleton
    fun provideFcmApiService(retrofit: Retrofit): FcmApiService {
        return retrofit.create(FcmApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFcmRepository(api: FcmApiService): FcmRepository {
        return FcmRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideRegisterFcmTokenUseCase(repository: FcmRepository) =
        RegisterFcmTokenUseCase(repository)

    @Provides
    @Singleton
    fun provideRegisterNotificationUseCase(repository: FcmRepository) =
        RegisterNotificationUseCase(repository)

    @Provides
    @Singleton
    fun provideSendNotificationsUseCase(repository: FcmRepository) =
        SendNotificationsUseCase(repository)
}
