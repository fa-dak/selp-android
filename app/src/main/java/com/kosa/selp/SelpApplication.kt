package com.kosa.selp

import android.app.Application
import com.iamport.sdk.domain.core.Iamport
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SelpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Iamport.create(this)
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
