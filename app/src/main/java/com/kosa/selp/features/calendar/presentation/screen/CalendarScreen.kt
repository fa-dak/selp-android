package com.kosa.selp.features.calendar.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.calendar.composable.AddEventDialog
import com.kosa.selp.features.calendar.composable.BottomAddButton
import com.kosa.selp.features.calendar.composable.CalendarEventDetailDialog
import com.kosa.selp.features.calendar.composable.CalendarEventListItem
import com.kosa.selp.features.calendar.composable.CalendarEventOverlay
import com.kosa.selp.features.calendar.composable.CalendarHeader
import com.kosa.selp.features.calendar.composable.CalendarMonthGrid
import com.kosa.selp.features.calendar.composable.CalendarWeekDays
import com.kosa.selp.features.calendar.config.CalendarConfig
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.calendar.model.EventInputState
import com.kosa.selp.features.calendar.presentation.viewModel.CalendarDataViewModel
import com.kosa.selp.features.calendar.presentation.viewModel.CalendarUiViewModel
import com.kosa.selp.features.calendar.presentation.viewModel.CalendarUserViewModel
import com.kosa.selp.features.calendar.utils.DateUtils
import com.kosa.selp.features.fcm.presentation.viewModel.FcmViewModel
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsViewModel
import com.kosa.selp.shared.theme.AppColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    uiViewModel: CalendarUiViewModel = hiltViewModel(),
    dataViewModel: CalendarDataViewModel = hiltViewModel(),
    contactsViewModel: MyContactsViewModel = hiltViewModel(),
    calendarUserViewModel: CalendarUserViewModel = hiltViewModel(),
    fcmViewModel: FcmViewModel = hiltViewModel(),
) {
    val selectedDate = uiViewModel.selectedDate.collectAsState().value
    val currentMonth = uiViewModel.currentMonth.collectAsState().value
    val events = dataViewModel.eventList.collectAsState().value
    val memberId = calendarUserViewModel.memberId.collectAsState().value


    val showOverlay = remember { mutableStateOf(false) }
    val showAddOverlay = remember { mutableStateOf(false) }
    val showDetailDialog = remember { mutableStateOf(false) }

    val eventInputState = remember { mutableStateOf(EventInputState()) }

    val selectedDateEvents = events.filter {
        val eventDate = DateUtils.parseDate(it.eventDate)
        DateUtils.isSameDay(eventDate, selectedDate)
    }
    val selectedEvent = remember { mutableStateOf<EventListResponseDto?>(null) }

    val config = CalendarConfig(
        selectedDate = selectedDate,
        onChange = { uiViewModel.updateSelectedDate(it) },
        events = events
    )

    LaunchedEffect(currentMonth) {
        val year = currentMonth.get(Calendar.YEAR)
        val month = currentMonth.get(Calendar.MONTH) + 1
        dataViewModel.getAllEvents(year, month)
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
                    calendar = currentMonth,
                    onPreviousMonth = {
                        uiViewModel.moveToPreviousMonth()
                        val updated = currentMonth.clone() as Calendar
                        updated.add(Calendar.MONTH, -1)
                        updated.set(Calendar.DAY_OF_MONTH, 1)
                        uiViewModel.updateSelectedDate(updated.time)
                        dataViewModel.getAllEvents(
                            updated.get(Calendar.YEAR),
                            updated.get(Calendar.MONTH) + 1
                        )
                    },
                    onNextMonth = {
                        uiViewModel.moveToNextMonth()
                        val updated = currentMonth.clone() as Calendar
                        updated.add(Calendar.MONTH, 1)
                        updated.set(Calendar.DAY_OF_MONTH, 1)
                        uiViewModel.updateSelectedDate(updated.time)
                        dataViewModel.getAllEvents(
                            updated.get(Calendar.YEAR),
                            updated.get(Calendar.MONTH) + 1
                        )
                    }
                )

                CalendarWeekDays()
                CalendarMonthGrid(month = currentMonth.time, config = config)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val list = selectedDateEvents.take(3)
                    itemsIndexed(list) { index, event ->
                        Column(
                            modifier = Modifier.clickable {
                                selectedEvent.value = event
                                showDetailDialog.value = true
                            }
                        ) {
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
                    onAddSchedule = { showAddOverlay.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        if (showOverlay.value) {
            CalendarEventOverlay(
                events = selectedDateEvents,
                onDismiss = { showOverlay.value = false },
                onEventClick = { event ->
                    selectedEvent.value = event
                    showDetailDialog.value = true
                }
            )
        }

        if (showDetailDialog.value && selectedEvent.value != null) {
            val event = selectedEvent.value!!

            CalendarEventDetailDialog(
                event = event,
                onDismiss = {
                    showDetailDialog.value = false
                    selectedEvent.value = null
                },
                onRecommendClick = {
                    val contactId = event.receiverInfoId
                    val anniversaryType = event.eventType
                    val eventId = event.eventId

                    showDetailDialog.value = false
                    selectedEvent.value = null
                    navController.navigate("surveyFunnelLite/$contactId?anniversary=$anniversaryType&eventId=$eventId")
                }
            )
        }

        if (showAddOverlay.value) {
            Dialog(
                onDismissRequest = { showAddOverlay.value = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false
                )
            ) {
                AddEventDialog(
                    selectedDate = selectedDate,
                    eventInputState = eventInputState,
                    contactsViewModel = contactsViewModel,
                    onDismiss = {
                        eventInputState.value = EventInputState()
                        showAddOverlay.value = false
                    },
                    onAdd = { dto ->
                        dataViewModel.registerEvent(dto, currentMonth)
                        eventInputState.value = EventInputState()
                        showAddOverlay.value = false

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val eventDate = LocalDate.parse(dto.eventDate, formatter)
                        val daysBefore = dto.notificationDaysBefore ?: 0
                        val sendDate = eventDate.minusDays(daysBefore.toLong()).format(formatter)

                        fcmViewModel.registerNotification(
                            memberId = memberId!!,
                            eventId = 116L, // 실제 Id 들어오면 대체
                            title = dto.eventName ?: "",
                            content = "${dto.eventName} 일정이 곧 다가와요!",
                            sendDate = sendDate
                        )
                    }
                )

            }
        }
    }
}
