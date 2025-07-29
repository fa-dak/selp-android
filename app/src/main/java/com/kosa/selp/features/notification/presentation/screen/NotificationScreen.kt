package com.kosa.selp.features.notification.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.notification.viewModel.NotificationViewModel
import com.kosa.selp.shared.theme.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(notifications) { noti ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = noti.title, fontWeight = FontWeight.Bold)
                    Text(text = noti.content)
                    Text(text = noti.createdDate, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
