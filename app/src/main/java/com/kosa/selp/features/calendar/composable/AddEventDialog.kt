package com.kosa.selp.features.calendar.composable

import EventRegisterRequestDto
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosa.selp.features.calendar.model.EventInputState
import com.kosa.selp.features.calendar.model.toDto
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsUiState
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsViewModel
import com.kosa.selp.features.survey.model.AnniversaryType
import com.kosa.selp.shared.theme.AppColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(
    selectedDate: Date,
    eventInputState: MutableState<EventInputState>,
    onDismiss: () -> Unit,
    onAdd: (EventRegisterRequestDto) -> Unit,
    contactsViewModel: MyContactsViewModel
) {
    val focusManager = LocalFocusManager.current
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val formattedDate = dateFormatter.format(selectedDate)

    val state = eventInputState.value

    var expandedType by remember { mutableStateOf(false) }
    var expandedReceiver by remember { mutableStateOf(false) }
    var expandedNotify by remember { mutableStateOf(false) }

    val notificationOptions = listOf(null, 1, 2, 3)

    val contactsState by contactsViewModel.uiState.collectAsState()

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
                        Text("취소", color = AppColor.primary)
                    }
                    Text(
                        "이벤트 추가",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AppColor.textPrimary
                    )
                    TextButton(
                        onClick = {
                            val dto = state.toDto(formattedDate)
                            onAdd(dto)
                            onDismiss()
                        },
                        enabled = state.eventName.isNotBlank()
                    ) {
                        Text(
                            "추가",
                            color = if (state.eventName.isNotBlank()) AppColor.primary else AppColor.textDisabled
                        )
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
                                    .clickable { focusManager.clearFocus() }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "날짜",
                                    color = AppColor.textPrimary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    formattedDate,
                                    color = AppColor.textSecondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }


                    item {
                        IosInput(title = "제목", placeholder = "이벤트 제목") {
                            IOSTextField(
                                value = state.eventName,
                                onValueChange = {
                                    eventInputState.value =
                                        eventInputState.value.copy(eventName = it)
                                },
                                placeholder = "이벤트 제목"
                            )
                        }
                    }

                    item {
                        val selectedReceiverName = remember(state.receiverInfoId, contactsState) {
                            if (state.receiverInfoId == null) {
                                "선택 안 됨"
                            } else {
                                val successState = contactsState as? MyContactsUiState.Success
                                val contact =
                                    successState?.contacts?.find { it.id == state.receiverInfoId }
                                contact?.nickname ?: "이름 없음"
                            }
                        }

                        IosPopupSelector(
                            title = "주변인",
                            selectedValue = selectedReceiverName,
                            isExpanded = expandedReceiver,
                            onToggle = {
                                focusManager.clearFocus()
                                expandedReceiver = !expandedReceiver
                                if (expandedReceiver) {
                                    expandedType = false
                                    expandedNotify = false
                                }
                            },
                            onDismiss = {
                                expandedReceiver = false
                            }
                        ) {
                            when (contactsState) {
                                is MyContactsUiState.Loading -> {
                                    IosPopupSelectableItem(
                                        text = "불러오는 중...",
                                        isSelected = false,
                                        onClick = {}
                                    )
                                }

                                is MyContactsUiState.Error -> {
                                    val message = (contactsState as MyContactsUiState.Error).message
                                    IosPopupSelectableItem(
                                        text = message,
                                        isSelected = false,
                                        onClick = {}
                                    )
                                }

                                is MyContactsUiState.Success -> {
                                    val contacts =
                                        (contactsState as MyContactsUiState.Success).contacts

                                    contacts.forEach { contact ->
                                        IosPopupSelectableItem(
                                            text = contact.nickname ?: "이름 없음",
                                            isSelected = state.receiverInfoId == contact.id,
                                            onClick = {
                                                eventInputState.value =
                                                    state.copy(receiverInfoId = contact.id)
                                                expandedReceiver = false
                                            }
                                        )
                                        if (contact != contacts.last()) IosDivider()
                                    }
                                }
                            }
                        }
                    }


                    item {
                        IosPopupSelector(
                            title = "이벤트 유형",
                            selectedValue = state.eventType?.label ?: "선택 안 됨",
                            isExpanded = expandedType,
                            onToggle = {
                                focusManager.clearFocus()
                                expandedType = !expandedType
                                if (expandedType) expandedNotify = false
                            },
                            onDismiss = {
                                expandedType = false
                            }
                        ) {
                            AnniversaryType.entries.forEach { type ->
                                IosPopupSelectableItem(
                                    text = type.label,
                                    isSelected = state.eventType == type,
                                    onClick = {
                                        eventInputState.value = state.copy(eventType = type)
                                        expandedType = false
                                    }
                                )
                                if (type != AnniversaryType.entries.last()) {
                                    IosDivider()
                                }
                            }
                        }
                    }

                    item {
                        IosPopupSelector(
                            title = "알림",
                            selectedValue = when (state.notificationDaysBefore) {
                                null -> "없음"
                                else -> "${state.notificationDaysBefore}일 전"
                            },
                            isExpanded = expandedNotify,
                            onToggle = {
                                focusManager.clearFocus()
                                expandedNotify = !expandedNotify
                                if (expandedNotify) expandedType = false
                            },
                            onDismiss = {
                                expandedNotify = false
                            }
                        ) {
                            notificationOptions.forEach { value ->
                                IosPopupSelectableItem(
                                    text = when (value) {
                                        null -> "없음"
                                        else -> "${value}일 전"
                                    },
                                    isSelected = state.notificationDaysBefore == value,
                                    onClick = {
                                        eventInputState.value =
                                            state.copy(notificationDaysBefore = value)
                                        expandedNotify = false
                                    }
                                )
                                if (value != notificationOptions.last()) {
                                    IosDivider()
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}



