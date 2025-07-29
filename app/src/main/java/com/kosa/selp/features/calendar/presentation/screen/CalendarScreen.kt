package com.kosa.selp.features.calendar.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.calendar.composable.BottomAddButton
import com.kosa.selp.features.calendar.composable.CalendarEventListItem
import com.kosa.selp.features.calendar.composable.CalendarHeader
import com.kosa.selp.features.calendar.composable.CalendarMonthGrid
import com.kosa.selp.features.calendar.composable.CalendarWeekDays
import com.kosa.selp.features.calendar.config.CalendarConfig
import com.kosa.selp.features.calendar.presentation.viewModel.CalendarDataViewModel
import com.kosa.selp.features.calendar.presentation.viewModel.CalendarUiViewModel
import com.kosa.selp.features.calendar.utils.DateUtils
import com.kosa.selp.shared.theme.AppColor
import java.util.Calendar

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    uiViewModel: CalendarUiViewModel = hiltViewModel(),
    dataViewModel: CalendarDataViewModel = hiltViewModel()
) {
    val selectedDate = uiViewModel.selectedDate.collectAsState().value
    val events = dataViewModel.eventList.collectAsState().value
    val showOverlay = remember { mutableStateOf(false) }
    val showAddDialog = remember { mutableStateOf(false) }

    val calendar = remember { Calendar.getInstance() }

    val selectedDateEvents = events.filter {
        val eventDate = DateUtils.parseDate(it.eventDate)
        DateUtils.isSameDay(eventDate, selectedDate)
    }

    val config = CalendarConfig(
        selectedDate = selectedDate,
        onChange = { uiViewModel.updateSelectedDate(it) },
        events = events
    )

    LaunchedEffect(Unit) {
        val today = Calendar.getInstance()
        dataViewModel.getAllEvents(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
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
                    calendar = calendar,
                    onPreviousMonth = { calendar.add(Calendar.MONTH, -1) },
                    onNextMonth = { calendar.add(Calendar.MONTH, 1) }
                )

                CalendarWeekDays()
                CalendarMonthGrid(month = calendar.time, config = config)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val list = selectedDateEvents.take(3)
                    itemsIndexed(list) { index, event ->
                        Column {
                            CalendarEventListItem(event)
                            if (index < list.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = AppColor.divider
                                )
                            }
                        }
                    }

                    if (selectedDateEvents.isNotEmpty()) {
                        item {
                            TextButton(
                                onClick = { showOverlay.value = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text("더보기", color = AppColor.primary)
                            }
                        }
                    }
                }

                BottomAddButton(
                    onAddSchedule = { showAddDialog.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = showOverlay.value,
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            BackHandler { showOverlay.value = false }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.white)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "전체 일정",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = { showOverlay.value = false }) {
                        Text("닫기", color = AppColor.primary)
                    }
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(selectedDateEvents) { index, event ->
                        Column {
                            CalendarEventListItem(event)
                            if (index < selectedDateEvents.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = AppColor.divider
                                )
                            }
                        }
                    }
                }
            }


            // 추가 정보 입력하는 오버레이
        }
    }
}


