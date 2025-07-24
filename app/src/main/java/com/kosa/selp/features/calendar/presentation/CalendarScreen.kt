package com.kosa.selp.features.calendar.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.calendar.viewModel.CalendarViewModel
import com.kosa.selp.shared.composable.calendar.CalendarHeader
import com.kosa.selp.shared.composable.calendar.CalendarMonthGrid
import com.kosa.selp.shared.composable.calendar.CalendarWeekDays
import com.kosa.selp.shared.composable.calendar.model.CalendarConfig

@Composable
fun CalendarScreen() {
    val viewModel: CalendarViewModel = hiltViewModel()

    val calendar = viewModel.calendar.value
    val selectedDate = viewModel.selectedDate.value
    val isModalVisible = viewModel.isEventModalVisible.value

    val config = CalendarConfig(
        selectedDate = selectedDate,
        onChange = { viewModel.onDateClick(it) },
        mode = "normal",
        range = LongRange(0, Long.MAX_VALUE),
        blockedTimestamps = emptyList()
    )

    Column(modifier = Modifier.padding(16.dp)) {
        CalendarHeader(
            calendar = calendar,
            onPreviousMonth = { viewModel.goToPreviousMonth() },
            onNextMonth = { viewModel.goToNextMonth() }
        )
        CalendarWeekDays()
        CalendarMonthGrid(
            month = calendar.time,
            config = config
        )
    }
}
