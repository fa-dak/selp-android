package com.kosa.selp.features.fcm.di

import com.kosa.selp.features.fcm.service.FcmTokenApiService
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
    fun provideFcmTokenApiService(retrofit: Retrofit): FcmTokenApiService {
        return retrofit.create(FcmTokenApiService::class.java)
    }
}