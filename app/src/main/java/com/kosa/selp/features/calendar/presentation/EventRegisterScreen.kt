package com.kosa.selp.features.calendar.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.calendar.dto.CalendarEventRegisterDto
import com.kosa.selp.features.calendar.viewModel.EventRegisterViewModel
import com.kosa.selp.features.calendar.viewModel.NavigationEvent
import com.kosa.selp.shared.theme.AppColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun EventRegisterScreen(
    navController: NavController,
    selectedDate: Date
) {
    val eventRegisterViewModel: EventRegisterViewModel = hiltViewModel()
    val navigationEvent by eventRegisterViewModel.navigationEvent.collectAsState(initial = null)

    val date = remember {
        mutableStateOf(
            selectedDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
    }
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }

    val receiverId = remember { mutableLongStateOf(-1) } // -1
    val receiverNickname = remember { mutableStateOf("") }

    val receiverInfoList by eventRegisterViewModel.receiverInfoList.collectAsState()
    val showDialog by eventRegisterViewModel.showReceiverRequiredDialog.collectAsState()
    val notifyBefore = remember { mutableStateOf("") }

    val isDropdownExpanded = remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { eventRegisterViewModel.dismissReceiverRequiredDialog() },
            confirmButton = {
                Text(
                    "확인",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { eventRegisterViewModel.dismissReceiverRequiredDialog() }
                )
            },
            title = { Text("알림") },
            text = { Text("받는 사람을 선택해주세요.") }
        )
    }
    LaunchedEffect(receiverInfoList) {
        Log.d("EventRegisterScreen", "받는 사람 목록 개수: ${receiverInfoList.size}")
    }
    LaunchedEffect(Unit) {
        eventRegisterViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToCalendar -> {
                    navController.navigate("calendar") // 캘린더 라우트로 이동
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.white)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("기념일 등록", style = MaterialTheme.typography.headlineSmall, color = AppColor.textPrimary)

        val dateDialogState = rememberMaterialDialogState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = androidx.compose.ui.Alignment.CenterStart
        ) {
            OutlinedTextField(
                value = date.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("날짜 (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            // Transparent clickable overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { dateDialogState.show() }
            )
        }

        // 날짜 선택 모달
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton("확인")
                negativeButton("취소")
            }
        ) {
            datepicker { pickedDate ->
                date.value = pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        }

        // 이름
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("기념일 이름") },
            modifier = Modifier.fillMaxWidth()
        )

        // 종류
        OutlinedTextField(
            value = type.value,
            onValueChange = { type.value = it },
            label = { Text("기념일 종류") },
            modifier = Modifier.fillMaxWidth()
        )

        // 받는 사람
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            OutlinedTextField(
                value = receiverNickname.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("받는 사람") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDropdownExpanded.value = true },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            )

            if (receiverInfoList.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("받는 사람이 없습니다.") },
                    onClick = {}
                )
            } else {
                receiverInfoList.forEach { info ->
                    DropdownMenuItem(
                        text = { Text(info.receiverNickname) },
                        onClick = {
                            receiverNickname.value = info.receiverNickname
                            receiverId.longValue = info.receiverInfoId
                            isDropdownExpanded.value = false
                        }
                    )
                }
            }
        }

        // 알림
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = notifyBefore.value,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        notifyBefore.value = input
                    }
                },
                label = { Text("알림 받을 시기") },
                modifier = Modifier
                    .weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors()
            )

            Text(
                text = "일 전",
                modifier = Modifier
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = AppColor.textPrimary
            )
        }

        Button(
            onClick = {
                Log.d("EventRegister", "등록 클릭됨")
                eventRegisterViewModel.registerEvent(
                    CalendarEventRegisterDto(
                        eventName = name.value,
                        eventType = type.value,
                        eventDate = date.value,
                        receiverInfoId = receiverId.value,
                        notificationDaysBefore = notifyBefore.value.toIntOrNull() ?: 0
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("등록")
        }
    }
}