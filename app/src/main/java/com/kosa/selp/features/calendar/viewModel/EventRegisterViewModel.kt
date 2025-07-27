package com.kosa.selp.features.calendar.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.calendar.apiService.EventApiService
import com.kosa.selp.features.calendar.apiService.ReceiverInfoApiService
import com.kosa.selp.features.calendar.dto.CalendarEventRegisterDto
import com.kosa.selp.features.calendar.dto.ReceiverInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NavigationEvent {
    object NavigateToCalendar : NavigationEvent()
}

@HiltViewModel
class EventRegisterViewModel @Inject constructor(
    private val receiverInfoApiService: ReceiverInfoApiService,
    private val eventApiService: EventApiService,
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent

    private val _receiverInfoList = MutableStateFlow(emptyList<ReceiverInfo>())
    val receiverInfoList = _receiverInfoList.asStateFlow()

    private val _showReceiverRequiredDialog = MutableStateFlow(false)
    val showReceiverRequiredDialog: StateFlow<Boolean> = _showReceiverRequiredDialog

    fun dismissReceiverRequiredDialog() {
        _showReceiverRequiredDialog.value = false
    }

    init {
        getReceiverInfoList()
    }

    fun getReceiverInfoList() {
        viewModelScope.launch {
            runCatching {
                receiverInfoApiService.getReceiverInfoList()
            }.onSuccess {
                _receiverInfoList.value = it
            }.onFailure {
                // TODO: 예외 처리
                Log.d("EventRegisterViewModel", "getReceiverInfoList: $it")
            }
        }
    }

    fun registerEvent(event: CalendarEventRegisterDto) {
        viewModelScope.launch {
            if (event.receiverInfoId == -1L) {
                _showReceiverRequiredDialog.value = true
                return@launch
            }
            if (event.eventName.isBlank() || event.eventType.isBlank()) {
                // TODO: 이름, 타입 유효성 확인 필요 → 각 항목마다 별도 다이얼로그 띄울 수도 있음
                Log.d("EventRegisterViewModel", "입력값 누락: eventName or eventType")
                return@launch
            }
            runCatching {
                eventApiService.registerEvent(event)

            }.onSuccess {
                // TODO: 성공 처리 (예: 메시지 표시, 화면 이동 등)
                Log.d("EventRegisterViewModel", "성공")
                _navigationEvent.emit(NavigationEvent.NavigateToCalendar)
            }.onFailure {
                // TODO: 실패 처리 (예: 에러 메시지 표시)
                Log.d("EventRegisterViewModel", "registerEvent: $it")
            }
        }
    }
}