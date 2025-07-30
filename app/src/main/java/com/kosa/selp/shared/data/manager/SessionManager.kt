package com.kosa.selp.shared.data.manager

import com.kosa.selp.shared.data.model.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    private val _currentUser = MutableStateFlow<UserInfo?>(null)
    val currentUser: StateFlow<UserInfo?> = _currentUser

    fun login(userInfo: UserInfo) {
        _currentUser.value = userInfo
    }

    fun logout() {
        _currentUser.value = null
        // TODO: 토큰 삭제 등 추가적인 로그아웃 처리
    }
}
