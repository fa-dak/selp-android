package com.kosa.selp.features.mypage.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsUiState
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsViewModel
import com.kosa.selp.shared.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyContactsScreen(
    navController: NavController,
    viewModel: MyContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val needsRefreshState = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("needsRefresh", false)?.collectAsState()
    val needsRefresh = needsRefreshState?.value

    LaunchedEffect(needsRefresh) {
        if (needsRefresh == true) {
            viewModel.fetchContacts()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("needsRefresh", false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "내 주변인",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.white
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("myContactDetail/-1") },
                containerColor = AppColor.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "주변인 추가")
            }
        },
        containerColor = AppColor.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is MyContactsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MyContactsUiState.Success -> {
                    ContactList(navController = navController, contacts = state.contacts)
                }
                is MyContactsUiState.Error -> {
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
fun ContactList(navController: NavController, contacts: List<Contact>) {
    if (contacts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("저장된 연락처가 없습니다.")
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "친구 ${contacts.size}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppColor.textSecondary,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(contacts) { contact ->
                    ContactItem(navController = navController, contact = contact)
                }
            }
        }
    }
}

@Composable
fun ContactItem(navController: NavController, contact: Contact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("myContactDetail/${contact.id}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.white)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getEmojiForRelationship(contact.relationship ?: "-"),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = contact.nickname ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${contact.relationship ?: "-"})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColor.textDisabled
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                val genderText = when (contact.gender) {
                    "MALE" -> "남성"
                    "FEMALE" -> "여성"
                    "NONE" -> "없음"
                    else -> "-"
                }
                Text(
                    text = "${contact.age?.toString() ?: "-"}세, $genderText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColor.textSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (contact.preferences?.isNotEmpty() == true) {
                    Text(
                        text = "선호도: ${contact.preferences.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColor.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun getEmojiForRelationship(relationship: String): String {
    return when (relationship.trim()) {
        "연인" -> "❤️"
        "부모님" -> "👨‍👩‍👧‍👦"
        "자식" -> "👨‍👩‍👧‍👦"
        "형제" -> "👧‍👦"
        "친인척" -> "👨‍👩‍👧"
        "친구" -> "🧑‍🤝‍🧑"
        "직장동료" -> "💼"
        "직장상사" -> "👔"
        "지인" -> "👋"
        else -> "👤"
    }
}