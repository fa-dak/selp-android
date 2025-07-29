package com.kosa.selp.features.login.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kosa.selp.features.fcm.data.remote.FcmApiService
import com.kosa.selp.features.login.data.model.KakaoLoginRequest
import com.kosa.selp.features.login.data.service.AuthApiService
import com.kosa.selp.shared.data.manager.SessionManager
import com.kosa.selp.shared.data.manager.AuthManager
import com.kosa.selp.shared.utils.JwtUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginEvent {
    data object LoginSuccess : LoginEvent()
    data class LoginFailure(val error: Throwable) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApiService: AuthApiService,
    private val fcmApiService: FcmApiService,
    private val authManager: AuthManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginEvent = Channel<LoginEvent>()
    val loginEvent = _loginEvent.receiveAsFlow()

    // 자동 로그인을 위한 상태 (null: 확인중, true: 로그인됨, false: 비로그인)
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        // ViewModel이 생성될 때 로그인 상태 확인
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val token = authManager.accessToken
            if (token != null) {
                JwtUtils.decodeUserInfo(token)?.let { sessionManager.login(it) }
                _isLoggedIn.value = true
            } else {
                _isLoggedIn.value = false
            }
            Log.d("LoginViewModel", "Login status checked: ${_isLoggedIn.value}")
        }
    }

    fun loginWithKakao(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("LoginViewModel", "카카오계정으로 로그인 실패", error)
                viewModelScope.launch { _loginEvent.send(LoginEvent.LoginFailure(error)) }
            } else if (token != null) {
                sendTokenToBackend(token)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("LoginViewModel", "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    sendTokenToBackend(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun sendTokenToBackend(token: OAuthToken) {
        Log.i("LoginViewModel", "카카오 로그인 성공. 백엔드에 토큰 전송 시도: ${token.accessToken}")
        viewModelScope.launch {
            try {
                val request = KakaoLoginRequest(accessToken = token.accessToken)
                val response = authApiService.loginWithKakao(request)
//                val fcmToken = FirebaseMessaging.getInstance().token.await()
//                Log.i("LoginViewModel", "FCM 토큰: $fcmToken")
//                fcmApiService.registerToken(FcmTokenRegisterRequestDto(token = fcmToken))
//                Log.i("LoginViewModel", "FCM 토큰 등록 완료")

                authManager.accessToken = response.accessToken
                authManager.refreshToken = response.refreshToken
                Log.i("LoginViewModel", "백엔드 로그인 성공 및 토큰 저장 완료")

                // 토큰 디코딩 및 세션에 사용자 정보 저장
                JwtUtils.decodeUserInfo(response.accessToken)?.let {
                    sessionManager.login(it)
                    Log.i("LoginViewModel", "사용자 정보 세션 저장 완료: $it")
                }

                _loginEvent.send(LoginEvent.LoginSuccess)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "백엔드 로그인 실패", e)
                _loginEvent.send(LoginEvent.LoginFailure(e))
            }
        }
    }
}