package com.kosa.selp.features.mypage.di

import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.data.service.MyPageApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyPageModule {

    @Provides
    @Singleton
    fun provideMyPageApiService(retrofit: Retrofit): MyPageApiService {
        return retrofit.create(MyPageApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageRepository(myPageApiService: MyPageApiService): MyPageRepository {
        return MyPageRepository(myPageApiService)
    }
}
