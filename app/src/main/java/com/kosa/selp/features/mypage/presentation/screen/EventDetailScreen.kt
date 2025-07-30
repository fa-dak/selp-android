package com.kosa.selp.features.mypage.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.mypage.model.EventModifyRequest
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageEvent
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageViewModel
import com.kosa.selp.shared.domain.model.EventType
import com.kosa.selp.shared.domain.model.Relationship
import com.kosa.selp.shared.theme.AppColor
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Long,
    navController: NavController,
    viewModel: MyPageViewModel
) {
    val context = LocalContext.current
    val eventDetail by viewModel.eventDetail.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    // --- UI State ---
    var eventName by remember { mutableStateOf("") }
    var selectedReceiver by remember { mutableStateOf<Contact?>(null) }
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }

    val calendar = Calendar.getInstance()
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    // --- Side Effects ---
    LaunchedEffect(eventId) {
        viewModel.fetchEventDetail(eventId)
        viewModel.fetchContacts()
    }

    LaunchedEffect(eventDetail, contacts) {
        if (eventDetail != null && contacts.isNotEmpty()) {
            val detail = eventDetail!!
            eventName = detail.eventName ?: ""
            selectedReceiver = contacts.find { it.id == detail.receiverId }
            selectedEventType = EventType.fromName(detail.eventType)

            try {
                val dateParts = detail.eventDate?.split("-")
                if (dateParts != null && dateParts.size == 3) {
                    selectedYear = dateParts[0].toInt()
                    selectedMonth = dateParts[1].toInt()
                    selectedDay = dateParts[2].toInt()
                }
            } catch (e: Exception) {
                // 날짜 파싱 실패 시 오늘 날짜 유지
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MyPageEvent.ModifySuccess -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                is MyPageEvent.ModifyFailed -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearEventDetail()
        }
    }

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("이벤트 수정", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = AppColor.primary)
                ) {
                    Text("취소")
                }
                Button(
                    onClick = {
                        val receiverId = selectedReceiver?.id
                        if (receiverId == null) {
                            Toast.makeText(context, "받는 사람을 선택해주세요.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val request = EventModifyRequest(
                            eventDate = "%d-%02d-%02d".format(selectedYear, selectedMonth, selectedDay),
                            eventName = eventName,
                            eventType = selectedEventType ?: EventType.ETC,
                            receiverInfoId = receiverId,
                            notificationDaysBefore = eventDetail?.notificationDaysBefore
                        )
                        viewModel.modifyEvent(request)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.primary)
                ) {
                    Text("수정")
                }
            }
        },
        containerColor = AppColor.background
    ) { innerPadding ->
        if (eventDetail == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("이벤트 정보를 불러오는 중...")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 날짜 선택
                DateSelection(
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    selectedDay = selectedDay,
                    onYearChange = { selectedYear = it },
                    onMonthChange = { selectedMonth = it },
                    onDayChange = { selectedDay = it }
                )

                // 제목
                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text("제목") },
                    modifier = Modifier.fillMaxWidth()
                )

                // 받는 사람
                Dropdown(
                    label = "받는 사람",
                    options = contacts,
                    selectedValue = selectedReceiver,
                    onValueChange = { selectedReceiver = it },
                    displayTransform = { contact ->
                        (if (contact.nickname.isNullOrBlank()) "-" else contact.nickname!!)
                    },
                    itemContent = { contact ->
                        ReceiverDropdownItem(contact = contact)
                    }
                )

                // 이벤트 종류
                EventTypeSelection(
                    selectedType = selectedEventType,
                    onTypeChange = { selectedEventType = it }
                )
            }
        }
    }
}

@Composable
private fun ReceiverDropdownItem(contact: Contact) {
    val relationshipInKorean = contact.relationship?.let { Relationship.fromDisplayName(it)?.displayName }
        ?: contact.relationship

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = if (contact.nickname.isNullOrBlank()) "-" else contact.nickname!!,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColor.textPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "($relationshipInKorean)",
            style = MaterialTheme.typography.bodyMedium,
            color = AppColor.textSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelection(
    selectedYear: Int, selectedMonth: Int, selectedDay: Int,
    onYearChange: (Int) -> Unit, onMonthChange: (Int) -> Unit, onDayChange: (Int) -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear - 10..currentYear + 10).toList()
    val months = (1..12).toList()
    val days = (1..Calendar.getInstance().apply { set(selectedYear, selectedMonth - 1, 1) }.getActualMaximum(Calendar.DAY_OF_MONTH)).toList()

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Dropdown(
            label = "년",
            options = years,
            selectedValue = selectedYear,
            onValueChange = onYearChange,
            modifier = Modifier.weight(1.5f)
        )
        Dropdown(
            label = "월",
            options = months,
            selectedValue = selectedMonth,
            onValueChange = onMonthChange,
            modifier = Modifier.weight(1f)
        )
        Dropdown(
            label = "일",
            options = days,
            selectedValue = selectedDay,
            onValueChange = onDayChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventTypeSelection(
    selectedType: EventType?,
    onTypeChange: (EventType) -> Unit
) {
    Dropdown(
        label = "이벤트 종류",
        options = EventType.entries,
        selectedValue = selectedType,
        displayTransform = { it.value },
        onValueChange = onTypeChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> Dropdown(
    label: String,
    options: List<T>,
    selectedValue: T?,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    displayTransform: (T) -> String = { it.toString() },
    itemContent: (@Composable (T) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue?.let(displayTransform) ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        if (itemContent != null) {
                            itemContent(option)
                        } else {
                            Text(displayTransform(option))
                        }
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
