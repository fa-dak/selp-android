package com.kosa.selp.features.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.calendar.viewModel.CalendarViewModel
import com.kosa.selp.shared.composable.calendar.CalendarHeader
import com.kosa.selp.shared.composable.calendar.CalendarMonthGrid
import com.kosa.selp.shared.composable.calendar.CalendarWeekDays
import com.kosa.selp.shared.composable.calendar.model.CalendarConfig
import com.kosa.selp.shared.theme.AppColor
import java.util.Collections.emptyList

data class CalendarEvent(
    val eventId: String,
    val eventName: String,
    val eventType: String,
    val receiverName: String,
    val notificationDaysBefore: Int,
    val date: java.util.Date
)

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier

) {
    val viewModel: CalendarViewModel = hiltViewModel()

    // 현재 달력, 선택된 날짜, 모달 표시 여부 상태 값
    val calendar = viewModel.calendar.value
    val selectedDate = viewModel.selectedDate.value
    val isModalVisible = viewModel.isEventModalVisible.value
    val events = viewModel.events.value
    val selectedDayEvents = viewModel.selectedDayEvents

    // 달력 설정 객체 (선택 날짜, 날짜 변경 이벤트, 모드, 범위, 비활성화 날짜, 이벤트)
    val config = CalendarConfig(
        selectedDate = selectedDate,
        onChange = { viewModel.onDateClick(it) }, // 날짜 클릭 시 ViewModel에 이벤트 전달
        mode = "normal",
        range = LongRange(0, Long.MAX_VALUE),
        blockedTimestamps = emptyList(),
        events = events
    )

    // 전체 화면을 차지하는 컬럼 레이아웃
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.white)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 달력 헤더 (월 이동 버튼 포함)
        CalendarHeader(
            calendar = calendar,
            onPreviousMonth = { viewModel.goToPreviousMonth() }, // 이전 달 이동
            onNextMonth = { viewModel.goToNextMonth() } // 다음 달 이동
        )
        // 요일 표시 줄
        CalendarWeekDays()
        // 달력 그리드 (날짜 선택 등) - 세로 공간을 꽉 채우게 weight(1f) 적용
        CalendarMonthGrid(
            month = calendar.time,
            config = config,
            modifier = Modifier.weight(1f)
        )


        // 선택된 날짜의 이벤트를 아래에 카드 형태로 표시
        if (selectedDayEvents.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedDayEvents.forEachIndexed { idx, event ->
                    androidx.compose.material3.Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = if (idx % 2 == 0) AppColor.primary.copy(alpha = 0.5f) else AppColor.secondary.copy(
                                alpha = 0.5f
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = event.eventName,
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    color = AppColor.textPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "유형: ${event.eventType}",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    color = AppColor.textSecondary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "대상: ${event.receiverNickname}",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    color = AppColor.textSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
        // 맨 아래 일정 추가하기 버튼
        Spacer(modifier = Modifier.height(2.dp))

        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = AppColor.surface),
            shape = androidx.compose.material3.CardDefaults.shape,
            elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(0.dp)
        ) {
            androidx.compose.material3.Button(
                onClick = { /* TODO: 일정 추가 기능 */ },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AppColor.surface),
                elevation = null,
                shape = androidx.compose.material3.CardDefaults.shape
            ) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Add,
                        contentDescription = null,
                        tint = AppColor.textPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "일정 추가하기",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = AppColor.textPrimary
                    )
                }
            }
        }
    }
}
