package com.kosa.selp.features.fcm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kosa.selp.MainActivity
import com.kosa.selp.R
import com.kosa.selp.features.fcm.data.request.FcmTokenRegisterRequestDto
import com.kosa.selp.features.fcm.domain.usecase.RegisterFcmTokenUseCase
import com.kosa.selp.features.fcm.model.DeviceType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelpMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var registerFcmTokenUseCase: RegisterFcmTokenUseCase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("FCM", "onMessageReceived 호출됨")

        val title = remoteMessage.notification?.title ?: "Selp 알림"
        val body = remoteMessage.notification?.body ?: ""

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        Log.i("FCM", "FCM 토큰 갱신됨: $token")

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val request =
                    FcmTokenRegisterRequestDto(token = token, deviceType = DeviceType.ANDROID)
                registerFcmTokenUseCase(request)
            }.onSuccess {
                Log.i("FCM", "토큰 갱신 등록 완료")
            }.onFailure {
                Log.e("FCM", "토큰 갱신 등록 실패", it)
            }
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "selp_channel"
        val notificationId = 1

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Selp Notification"
            val descriptionText = "Selp 알림 채널"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.selp_character)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("FCM", "알림 권한 없음 → 알림 생략됨")
            return
        }

        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }
}
