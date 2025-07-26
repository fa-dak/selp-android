package com.kosa.selp.features.login.data.service

import com.kosa.selp.features.login.data.model.KakaoLoginRequest
import com.kosa.selp.features.login.data.model.KakaoLoginResponse
import com.kosa.selp.shared.data.interceptor.NoAuth
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @NoAuth
    @POST("/oauth/kakao")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequest
    ): KakaoLoginResponse
}
