package com.kosa.selp.features.gift.di

import com.kosa.selp.features.gift.data.remote.GiftApiService
import com.kosa.selp.features.gift.data.repositoryImpl.GiftRepositoryImpl
import com.kosa.selp.features.gift.domain.repository.GiftRepository
import com.kosa.selp.features.gift.domain.usecase.GetGiftBundleDetailUseCase
import com.kosa.selp.features.gift.domain.usecase.GetGiftBundleRecommendMessagesUseCase
import com.kosa.selp.features.gift.domain.usecase.RecommendGiftBundleUseCase
import com.kosa.selp.features.gift.domain.usecase.SaveGiftBundleFromCalendarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GiftNetworkModule {

    @Provides
    @Singleton
    fun provideGiftApiService(retrofit: Retrofit): GiftApiService {
        return retrofit.create(GiftApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGiftRepository(
        giftApiService: GiftApiService
    ): GiftRepository {
        return GiftRepositoryImpl(giftApiService)
    }

    @Provides
    @Singleton
    fun provideGetGiftBundleDetailUseCase(
        repository: GiftRepository
    ): GetGiftBundleDetailUseCase {
        return GetGiftBundleDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRecommendedMessagesUseCase(
        repository: GiftRepository
    ): GetGiftBundleRecommendMessagesUseCase {
        return GetGiftBundleRecommendMessagesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRecommendGiftBundleUseCase(
        repository: GiftRepository
    ): RecommendGiftBundleUseCase {
        return RecommendGiftBundleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveGiftBundleFromCalendarUseCase(
        repository: GiftRepository
    ): SaveGiftBundleFromCalendarUseCase {
        return SaveGiftBundleFromCalendarUseCase(repository)
    }
}
