package com.kosa.selp.features.mypage.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import com.kosa.selp.shared.data.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyPageEvent {
    data object LogoutSuccess : MyPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val authManager: AuthManager // AuthManager 주입
) : ViewModel() {

    private val _giftBundles = MutableStateFlow<List<GiftBundleResponse>>(emptyList())
    val giftBundles: StateFlow<List<GiftBundleResponse>> = _giftBundles

    private val _event = Channel<MyPageEvent>()
    val event = _event.receiveAsFlow()

    fun fetchMyGiftBundles() {
        viewModelScope.launch {
            try {
                val bundles = myPageRepository.getMyGiftBundles()
                _giftBundles.value = bundles
                Log.d("MyPageViewModel", "성공: ${bundles.size}개 아이템 로드")
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "실패: ${e.message}")
            }
        }
    }

    // 로그아웃 함수 추가
    fun logout() {
        viewModelScope.launch {
            authManager.clearTokens() // 저장된 토큰 삭제
            _event.send(MyPageEvent.LogoutSuccess) // UI에 로그아웃 성공 이벤트 전송
        }
    }
}