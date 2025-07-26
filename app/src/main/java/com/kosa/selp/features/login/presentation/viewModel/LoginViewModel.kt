package com.kosa.selp.features.login.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kosa.selp.features.fcm.model.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.service.FcmTokenApiService
import com.kosa.selp.features.login.data.model.KakaoLoginRequest
import com.kosa.selp.features.login.data.service.AuthApiService
import com.kosa.selp.shared.data.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApiService: AuthApiService,
    private val fcmTokenApiService: FcmTokenApiService,
    private val authManager: AuthManager
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun loginWithKakao(context: Context) {
        viewModelScope.launch {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    handleLoginError(error)
                } else if (token != null) {
                    handleLoginSuccess(token)
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    } else if (token != null) {
                        handleLoginSuccess(token)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    private fun handleLoginSuccess(token: OAuthToken) {
        Log.i("LoginViewModel", "카카오 로그인 성공. 백엔드에 토큰 전송 시도: ${token.accessToken}")
        viewModelScope.launch {
            try {
                // 백엔드에 카카오 accessToken 전송
                val request = KakaoLoginRequest(accessToken = token.accessToken)
                val response = authApiService.loginWithKakao(request)
//                val fcmToken = FirebaseMessaging.getInstance().token.await()
//                Log.i("LoginViewModel", "FCM 토큰: $fcmToken")
//                fcmTokenApiService.registerToken(FcmTokenRegisterRequestDto(token = fcmToken))
//                Log.i("LoginViewModel", "FCM 토큰 등록 완료")

                // 서버로부터 받은 accessToken, refreshToken 저장
                authManager.accessToken = response.accessToken
                authManager.refreshToken = response.refreshToken
                Log.i("LoginViewModel", "백엔드 로그인 성공 및 토큰 저장 완료")

                _loginEvent.emit(LoginEvent.LoginSuccess)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "백엔드 로그인 실패", e)
                _loginEvent.emit(LoginEvent.LoginFailure)
            }
        }
    }

    private fun handleLoginError(error: Throwable) {
        Log.e("LoginViewModel", "카카오계정으로 로그인 실패", error)
        viewModelScope.launch {
            _loginEvent.emit(LoginEvent.LoginFailure)
        }
    }
}

sealed class LoginEvent {
    data object LoginSuccess : LoginEvent()
    data object LoginFailure : LoginEvent()
}
