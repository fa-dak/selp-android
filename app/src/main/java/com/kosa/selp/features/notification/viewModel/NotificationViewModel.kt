package com.kosa.selp.features.notification.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.notification.data.response.NotificationFindResponseDto
import com.kosa.selp.features.notification.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationFindResponseDto>>(emptyList())
    val notifications: StateFlow<List<NotificationFindResponseDto>> = _notifications

    init {
        viewModelScope.launch {
            runCatching {
                repository.getNotifications()
            }.onSuccess {
                println("✅ 알림 수: ${it.size}")
                it.forEach { noti -> println("🔔 ${noti.title} - ${noti.createdDate}") }
                _notifications.value = it
            }.onFailure {
                println("❌ 알림 요청 실패: ${it.message}")
            }
        }
    }
}
