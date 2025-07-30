package com.kosa.selp.features.fcm.presentation.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.kosa.selp.features.fcm.data.request.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.data.request.NotificationRequestDto
import com.kosa.selp.features.fcm.domain.usecase.RegisterFcmTokenUseCase
import com.kosa.selp.features.fcm.domain.usecase.RegisterNotificationUseCase
import com.kosa.selp.features.fcm.domain.usecase.SendNotificationsUseCase
import com.kosa.selp.features.fcm.model.DeviceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FcmViewModel @Inject constructor(
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val registerNotificationUseCase: RegisterNotificationUseCase,
    private val sendNotificationsUseCase: SendNotificationsUseCase
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerNotification(
        memberId: Long,
        eventId: Long,
        title: String,
        content: String,
        sendDate: String
    ) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val targetDate = LocalDate.parse(sendDate, formatter)
            val today = LocalDate.now()

            if (targetDate.isBefore(today)) return@launch

            runCatching {
                val request = NotificationRequestDto(
                    memberId = memberId,
                    eventId = eventId,
                    title = title,
                    content = content,
                    sendDate = sendDate
                )
                registerNotificationUseCase(request)
            }.onFailure {
                Log.w("FcmViewModel", "알림 등록 실패: ${it.localizedMessage}")
            }
        }
    }

    fun registerFcmToken() {
        viewModelScope.launch {
            runCatching {
                val token = FirebaseMessaging.getInstance().token.await()
                val request = FcmTokenRegisterRequestDto(
                    token = token,
                    deviceType = DeviceType.ANDROID
                )
                registerFcmTokenUseCase(request)
                Log.i("FcmViewModel", "FCM 토큰 등록 성공 → $token")
            }.onFailure {
                Log.w("FcmViewModel", "FCM 토큰 등록 실패", it)
            }
        }
    }

    fun sendNotifications() {
        viewModelScope.launch {
            runCatching {
                sendNotificationsUseCase()
            }.onFailure {
                Log.w("FcmViewModel", "알림 발송 실패", it)
            }
        }
    }
}
