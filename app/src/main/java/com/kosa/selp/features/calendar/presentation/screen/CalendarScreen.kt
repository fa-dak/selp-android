package com.kosa.selp.features.calendar.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.calendar.composable.CalendarHeader
import com.kosa.selp.features.calendar.composable.CalendarMonthGrid
import com.kosa.selp.features.calendar.composable.CalendarWeekDays
import com.kosa.selp.features.calendar.config.CalendarConfig
import com.kosa.selp.shared.theme.AppColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ScheduleEvent(
    val id: String,
    val title: String,
    val time: String? = null,
    val color: Color = Color(0xFF9C88FF),
    val date: Date
)

@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }
    val selectedDate = remember { mutableStateOf(Date()) }
    val inputText = remember { mutableStateOf("") }

    val config = CalendarConfig(
        selectedDate = selectedDate.value,
        onChange = { selectedDate.value = it },
        events = emptyList()
    )

    val allEvents = listOf(
        ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ), ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ), ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ), ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ), ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "2",
            title = "워크샵",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ), ScheduleEvent(
            id = "1",
            title = "하루 종일",
            color = Color(0xFF9C88FF),
            date = selectedDate.value
        ),
        ScheduleEvent(
            id = "3",
            title = "경평",
            time = "오후 5:00",
            color = Color(0xFFFFB800),
            date = getTomorrowDate(selectedDate.value)
        )
    )

    // 선택된 날짜의 일정만 필터링
    val selectedDateEvents = allEvents.filter { event ->
        isSameDay(event.date, selectedDate.value)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.white)
    ) {
        // 캘린더 부분
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CalendarHeader(
                calendar = calendar.value,
                onPreviousMonth = {
                    calendar.value = (calendar.value.clone() as Calendar).apply {
                        add(Calendar.MONTH, -1)
                    }
                },
                onNextMonth = {
                    calendar.value = (calendar.value.clone() as Calendar).apply {
                        add(Calendar.MONTH, 1)
                    }
                }
            )

            CalendarWeekDays()

            CalendarMonthGrid(
                month = calendar.value.time,
                config = config
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(selectedDateEvents) { event ->
                ScheduleListItem()
            }

        }

        BottomAddButton(
            selectedDate = selectedDate.value,
            onAddSchedule = {
                // TODO: 일정 추가 화면으로 이동
                // navController.navigate("add_schedule/${selectedDate.value.time}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun ScheduleListItem(
    showDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF9C88FF))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                // 일정 타이틀 (임시)
                Text(
                    text = "워크샵",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = AppColor.textPrimary
                    )
                )

                // 태그 UI (관계용)
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("부모님", "친구").forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppColor.secondary.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AppColor.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }

        if (showDivider) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppColor.divider)
            )
        }
    }
}

@Composable
fun BottomAddButton(
    selectedDate: Date,
    onAddSchedule: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("M월 d일", Locale.KOREAN)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = AppColor.background,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onAddSchedule)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "추가",
                tint = AppColor.textPrimary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = "추가",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColor.textPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun getTomorrowDate(date: Date): Date {
    val calendar = Calendar.getInstance().apply {
        time = date
        add(Calendar.DAY_OF_MONTH, 1)
    }
    return calendar.time
}