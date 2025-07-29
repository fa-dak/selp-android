package com.kosa.selp.features.calendar.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.calendar.composable.BottomAddButton
import com.kosa.selp.features.calendar.composable.CalendarEventListItem
import com.kosa.selp.features.calendar.composable.CalendarHeader
import com.kosa.selp.features.calendar.composable.CalendarMonthGrid
import com.kosa.selp.features.calendar.composable.CalendarWeekDays
import com.kosa.selp.features.calendar.config.CalendarConfig
import com.kosa.selp.features.calendar.utils.DateUtils
import com.kosa.selp.shared.theme.AppColor
import java.util.Calendar
import java.util.Date

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
    )

    val selectedDateEvents = allEvents.filter { event ->
        DateUtils.isSameDay(event.date, selectedDate.value)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.white)
    ) {
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
            itemsIndexed(selectedDateEvents) { index, event ->
                Column {
                    CalendarEventListItem(
                        title = event.title,
                        relationTag = "부모님",
                        extraTags = listOf("감성적", "건강", "집들이")
                    )
                    if (index < selectedDateEvents.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp,
                            color = AppColor.divider
                        )
                    }
                }
            }

        }

        BottomAddButton(
            onAddSchedule = { /* TODO: 일정 추가 화면으로 이동 */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}







