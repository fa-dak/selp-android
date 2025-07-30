package com.kosa.selp.features.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.survey.model.AnniversaryType
import com.kosa.selp.shared.theme.AppColor
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CalendarEventDetailDialog(
    event: EventListResponseDto,
    onDismiss: () -> Unit,
    onRecommendClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val formattedDate = event.eventDate

    val eventName = event.eventName ?: ""
    val receiverName = event.receiverNickname ?: "선택 안 됨"
    val eventType = event.eventType?.let {
        AnniversaryType.fromCode(it)?.label ?: "선택 안 됨"
    } ?: "선택 안 됨"
    val notifyDays = event.notificationDaysBefore?.let { "${it}일 전" } ?: "없음"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .align(Alignment.BottomCenter)
                    .background(
                        AppColor.background,
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("닫기", color = AppColor.primary)
                        }
                        Text(
                            "이벤트",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = AppColor.textPrimary
                        )
                        TextButton(onClick = onRecommendClick) {
                            Text("선물 추천", color = AppColor.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            bottom = WindowInsets.systemBars.getBottom(LocalDensity.current).dp + 16.dp
                        )
                    ) {
                        item {
                            ReadOnlyItem(title = "날짜", value = formattedDate ?: "날짜 없음")
                        }

                        item {
                            ReadOnlyItem(title = "제목", value = eventName)
                        }

                        item {
                            ReadOnlyItem(title = "주변인", value = receiverName)
                        }

                        item {
                            ReadOnlyItem(title = "이벤트 유형", value = eventType)
                        }

                        item {
                            ReadOnlyItem(title = "알림", value = notifyDays)
                        }

                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadOnlyItem(title: String, value: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        color = AppColor.white
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, color = AppColor.textPrimary, style = MaterialTheme.typography.bodyMedium)
            Text(value, color = AppColor.textSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
