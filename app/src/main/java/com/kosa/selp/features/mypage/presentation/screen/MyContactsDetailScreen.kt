package com.kosa.selp.features.mypage.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.R
import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsDetailEvent
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsDetailUiState
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsDetailViewModel
import com.kosa.selp.shared.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyContactsDetailScreen(
    navController: NavController,
    viewModel: MyContactsDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MyContactsDetailEvent.NavigateUp -> {
                    if (event.needsRefresh) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("needsRefresh", true)
                    }
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "주변인 상세 정보",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.deleteContact() }) {
                        Text(
                            text = "삭제",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.white
                )
            )
        },
        containerColor = AppColor.background
    ) { paddingValues ->
        when (val state = uiState) {
            is MyContactsDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is MyContactsDetailUiState.Success -> {
                ContactDetailContent(
                    modifier = Modifier.padding(paddingValues),
                    contact = state.contact,
                    viewModel = viewModel,
                    navController = navController
                )
            }

            is MyContactsDetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "오류가 발생했습니다: ${state.message}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactDetailContent(
    modifier: Modifier = Modifier,
    contact: Contact,
    viewModel: MyContactsDetailViewModel,
    navController: NavController
) {
    var nickname by remember { mutableStateOf(contact.nickname) }
    var age by remember { mutableStateOf(contact.age.toString()) }
    var gender by remember { mutableStateOf(contact.gender ?: "NONE") }
    var relationship by remember { mutableStateOf(contact.relationship) }
    var preferences by remember { mutableStateOf(contact.preferences.joinToString(", ")) }
    var detail by remember { mutableStateOf(contact.detail ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DetailInputItem(label = "닉네임", value = nickname, onValueChange = { nickname = it })
            DetailInputItem(label = "나이", value = age, onValueChange = { newAge ->
                if (newAge.all { it.isDigit() }) {
                    age = newAge
                }
            })
            DetailInputItem(label = "성별", value = gender, onValueChange = { gender = it })
            DetailInputItem(label = "관계", value = relationship, onValueChange = { relationship = it })
            DetailInputItem(label = "선호도", value = preferences, onValueChange = { preferences = it })
            DetailInputItem(label = "상세 설명", value = detail, onValueChange = { detail = it })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {0
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
                    viewModel.saveContact(
                        nickname = nickname,
                        age = age,
                        gender = gender,
                        relationship = relationship,
                        preferences = preferences,
                        detail = detail
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.primary)
            ) {
                Text("저장")
            }
        }
    }
}

@Composable
fun DetailInputItem(label: String, value: String, onValueChange: (String) -> Unit) {
    when (label) {
        "성별" -> GenderInputItem(selectedValue = value, onValueChange = onValueChange)
        "관계" -> RelationshipInputItem(selectedValue = value, onValueChange = onValueChange)
        else -> {
            val keyboardType = if (label == "나이") KeyboardType.Number else KeyboardType.Text
            TextInputItem(label = label, value = value, onValueChange = onValueChange, keyboardType = keyboardType)
        }
    }
}

@Composable
fun GenderInputItem(selectedValue: String, onValueChange: (String) -> Unit) {
    val genderOptions = mapOf("남성" to "MALE", "여성" to "FEMALE", "없음" to "NONE")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gender_icon),
                    contentDescription = "성별 아이콘",
                    modifier = Modifier.size(20.dp),
                    tint = AppColor.textDisabled
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                genderOptions.forEach { (text, genderValue) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 30.dp, end = 16.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onValueChange(genderValue) }
                    ) {
                        RadioButton(
                            selected = (selectedValue == genderValue),
                            onClick = { onValueChange(genderValue) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppColor.primary,
                                unselectedColor = AppColor.textDisabled
                            )
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        Divider(
            color = AppColor.textDisabled.copy(alpha = 0.2f),
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationshipInputItem(selectedValue: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val relationshipOptions = listOf(
        "연인" to "연인",
        "부모님" to "부모님",
        "자식" to "자식",
        "형제" to "형제",
        "친인척" to "친인척",
        "친구" to "친구",
        "직장동료" to "직장동료",
        "직장상사" to "직장상사",
        "지인" to "지인"
    )
    val currentDisplayValue = relationshipOptions.find { it.second == selectedValue }?.first ?: selectedValue

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "관계 아이콘",
                    modifier = Modifier.size(20.dp),
                    tint = AppColor.textDisabled
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = currentDisplayValue,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    relationshipOptions.forEach { (text, value) ->
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = {
                                onValueChange(value)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Divider(
            color = if (expanded) AppColor.primary else AppColor.textDisabled.copy(alpha = 0.2f),
            thickness = 1.dp
        )
    }
}

@Composable
fun TextInputItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val isNickname = label == "닉네임"

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp), // Ensure a minimum height for alignment
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!isNickname) {
                Box(
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (label) {
                        "선호도" -> Icon(
                            painter = painterResource(id = R.drawable.preferences_icon),
                            contentDescription = "선호도 아이콘",
                            modifier = Modifier.size(20.dp),
                            tint = AppColor.textDisabled
                        )
                        "상세 설명" -> Icon(
                            painter = painterResource(id = R.drawable.note_icon),
                            contentDescription = "상세 설명 아이콘",
                            modifier = Modifier.size(20.dp),
                            tint = AppColor.textDisabled
                        )
                        else -> Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall,
                            color = AppColor.textDisabled
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = (if (isNickname) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge).copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush = SolidColor(AppColor.primary),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 20.dp, bottom = 20.dp, end = 16.dp),
                        contentAlignment = Alignment.CenterStart // Vertically center the text
                    ) {
                        if (value.isEmpty() && isNickname) {
                            Text(
                                text = "닉네임",
                                style = MaterialTheme.typography.headlineSmall,
                                color = AppColor.textDisabled
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Divider(
            color = if (isFocused) AppColor.primary else AppColor.textDisabled.copy(alpha = 0.2f),
            thickness = 1.dp
        )
    }
}