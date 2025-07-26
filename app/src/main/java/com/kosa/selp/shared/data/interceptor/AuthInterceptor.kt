package com.kosa.selp.shared.data.interceptor

import com.kosa.selp.shared.data.manager.AuthManager
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

/**
 * 인증이 필요 없는 API 요청 함수에 이 어노테이션을 붙여주세요.
 * 이 어노테이션이 붙은 API는 요청 헤더에 토큰이 담기지 않습니다.
 * (예: 로그인, 회원가입)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NoAuth

class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // @NoAuth 어노테이션이 있는지 확인
        val noAuth = originalRequest.tag(Invocation::class.java)
            ?.method()
            ?.isAnnotationPresent(NoAuth::class.java) ?: false

        // 토큰이 있고, @NoAuth 어노테이션이 없다면 헤더 추가
        val token = authManager.accessToken
        if (token != null && !noAuth) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }

        // 그 외의 경우는 원래 요청 그대로 진행
        return chain.proceed(originalRequest)
    }
}
