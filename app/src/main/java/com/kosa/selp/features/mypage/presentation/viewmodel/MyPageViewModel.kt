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
    private val authManager: AuthManager
) : ViewModel() {

    // StateFlow의 타입을 그룹화된 Map으로 변경
    private val _giftBundles = MutableStateFlow<Map<String, List<GiftBundleResponse>>>(emptyMap())
    val giftBundles: StateFlow<Map<String, List<GiftBundleResponse>>> = _giftBundles

    private val _event = Channel<MyPageEvent>()
    val event = _event.receiveAsFlow()

    fun fetchMyGiftBundles() {
        viewModelScope.launch {
            try {
                // 1. Repository에서 평평한 리스트를 가져옴
                val bundlesList = myPageRepository.getMyGiftBundles()
                // 2. 'createdDate'를 기준으로 데이터를 그룹화하여 Map으로 변환
                val groupedBundles = bundlesList.groupBy { it.createdDate }
                // 3. 그룹화된 데이터를 StateFlow에 업데이트
                _giftBundles.value = groupedBundles
                Log.d("MyPageViewModel", "성공: ${groupedBundles.size}개 날짜 그룹 로드")
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