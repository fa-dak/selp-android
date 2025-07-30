package com.kosa.selp.features.mypage.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.model.EventDetailResponse
import com.kosa.selp.features.mypage.model.EventModifyRequest
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import com.kosa.selp.shared.data.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyPageEvent {
    data object LogoutSuccess : MyPageEvent()
    data class ModifySuccess(val message: String) : MyPageEvent()
    data class ModifyFailed(val message: String) : MyPageEvent()
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

    // 상세 꾸러미 정보를 담을 StateFlow
    private val _giftBundleDetail = MutableStateFlow<GiftBundleResponse?>(null)
    val giftBundleDetail = _giftBundleDetail.asStateFlow()

    // 이벤트 상세 정보를 담을 StateFlow
    private val _eventDetail = MutableStateFlow<EventDetailResponse?>(null)
    val eventDetail = _eventDetail.asStateFlow()

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

    fun fetchGiftBundleDetail(bundleId: Long) {
        viewModelScope.launch {
            try {
                val detail = myPageRepository.getMyGiftBundleDetail(bundleId)
                Log.d("MyPageViewModel", "GiftBundleDetail DTO: $detail") // DTO 전체 로그 출력
                _giftBundleDetail.value = detail
                Log.d("MyPageViewModel", "상세 정보 로드 성공: ${detail.eventName}")
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "상세 정보 로드 실패: ${e.message}")
            }
        }
    }

    fun fetchEventDetail(eventId: Long) {
        viewModelScope.launch {
            try {
                val detail = myPageRepository.getEventDetail(eventId)
                _eventDetail.value = detail
                Log.d("MyPageViewModel", "이벤트 상세 정보 로드 성공: ${detail.eventName}")
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "이벤트 상세 정보 로드 실패: ${e.message}")
            }
        }
    }

    fun modifyEvent(request: EventModifyRequest) {
        viewModelScope.launch {
            try {
                val eventId = _eventDetail.value?.eventId ?: throw IllegalStateException("Event ID is null")
                myPageRepository.modifyEvent(eventId, request)
                _event.send(MyPageEvent.ModifySuccess("이벤트가 성공적으로 수정되었습니다."))
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "이벤트 수정 실패", e)
                _event.send(MyPageEvent.ModifyFailed("이벤트 수정에 실패했습니다: ${e.message}"))
            }
        }
    }

    // 화면이 사라질 때 상세 정보를 초기화하는 함수
    fun clearGiftBundleDetail() {
        _giftBundleDetail.value = null
    }

    fun clearEventDetail() {
        _eventDetail.value = null
    }

    // 로그아웃
    fun logout() {
        viewModelScope.launch {
            authManager.clearTokens() // 저장된 토큰 삭제
            _event.send(MyPageEvent.LogoutSuccess) // UI에 로그아웃 성공 이벤트 전송
        }
    }
}
