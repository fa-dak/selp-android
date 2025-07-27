package com.kosa.selp.features.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.calendar.dto.CalendarEvent
import com.kosa.selp.features.calendar.viewModel.CalendarViewModel
import com.kosa.selp.shared.composable.calendar.CalendarHeader
import com.kosa.selp.shared.composable.calendar.CalendarMonthGrid
import com.kosa.selp.shared.composable.calendar.CalendarWeekDays
import com.kosa.selp.shared.composable.calendar.model.CalendarConfig
import com.kosa.selp.shared.theme.AppColor
import java.util.Calendar
import java.util.Collections.emptyList

@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: CalendarViewModel = hiltViewModel()
    val currentMonthOffset = 1000
    val pagerState = rememberPagerState(
        initialPage = currentMonthOffset,
        pageCount = { Int.MAX_VALUE }
    )
    // 현재 달력, 선택된 날짜, 모달 표시 여부 상태 값
    val calendar = viewModel.calendar.value
    val selectedDate = viewModel.selectedDate.value
    val isModalVisible = viewModel.isEventModalVisible.value
    val events = viewModel.events.value
    val selectedDayEvents = viewModel.selectedDayEvents
    val selectedEventForDetail = remember { mutableStateOf<CalendarEvent?>(null) }

    // 기준 날짜를 remember로 고정
    val baseCalendar = remember {
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1) // 날짜를 1일로 고정하면 월 기준 정확
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        val offset = pagerState.currentPage - currentMonthOffset
        val newCalendar = (baseCalendar.clone() as Calendar).apply {
            add(Calendar.MONTH, offset)
        }
        viewModel.setCalendar(newCalendar)
    }

    selectedEventForDetail.value?.let { event ->
        AlertDialog(
            onDismissRequest = { selectedEventForDetail.value = null },
            confirmButton = {
                Button(onClick = { selectedEventForDetail.value = null }) {
                    Text("확인")
                }
            },
            title = { Text("이벤트 상세") },
            text = {
                Column {
                    Text("제목: ${event.eventName}")
                    Text("유형: ${event.eventType}")
                    Text("일자: ${event.eventDate}")
                    Text("받는 사람: ${event.receiverNickname}")
                    Text("알림: ${event.notificationDaysBefore}일 전")
                }
            }
        )
    }

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
            .padding(horizontal = 24.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val offset = page - currentMonthOffset
            val targetCalendar = (baseCalendar.clone() as Calendar).apply {
                add(Calendar.MONTH, offset)
            }

            CalendarMonthGrid(
                month = targetCalendar.time,
                config = config.copy(onChange = { viewModel.onDateClick(it) }),
                modifier = Modifier.fillMaxSize()
            )
        }


        // 선택된 날짜의 이벤트를 아래에 카드 형태로 표시
        if (isModalVisible && !selectedDayEvents.isEmpty()) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 130.dp)
                    .verticalScroll(scrollState)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedDayEvents.forEachIndexed { idx: Int, event: CalendarEvent ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedEventForDetail.value = event },
                        colors = CardDefaults.cardColors(
                            containerColor = if (idx % 2 == 0) AppColor.primary.copy(alpha = 0.5f) else AppColor.secondary.copy(
                                alpha = 0.5f
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = event.eventName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AppColor.textPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "유형: ${event.eventType}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColor.textSecondary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "대상: ${event.receiverNickname}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColor.textSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.surface),
                shape = CardDefaults.shape,
                elevation = CardDefaults.elevatedCardElevation(0.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("eventRegister/${selectedDate.time}")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.surface),
                    elevation = null,
                    shape = CardDefaults.shape
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = AppColor.textPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "일정 추가하기",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColor.textPrimary
                        )
                    }
                }
            }
        }
    }
}
