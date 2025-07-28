package com.kosa.selp.features.mypage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageEvent
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageViewModel
import com.kosa.selp.shared.theme.AppColor

@Composable
fun MyPageScreen(
    navController: NavController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MyPageEvent.LogoutSuccess -> {
                    onLogout()
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.white)
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        item {
            Text(
                text = "마이페이지",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp)) // 타이틀과 메뉴 사이 간격
        }

        item {
            MyPageMenuItem(
                icon = Icons.Default.CardGiftcard,
                text = "내 선물 꾸러미",
                onClick = { navController.navigate("giftBundleList") }
            )
        }
        item {
            MyPageMenuItem(
                icon = Icons.Default.People,
                text = "내 주변인",
                onClick = { navController.navigate("myContacts") }
            )
        }
        item {
            MyPageMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                text = "로그아웃",
                onClick = { viewModel.logout() }
            )
        }
    }
}

@Composable
private fun MyPageMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // <-- 'horizontalArrangement =' 추가하여 수정
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = text, tint = AppColor.textPrimary)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = text, style = MaterialTheme.typography.bodyLarge)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = AppColor.textDisabled,
                modifier = Modifier.height(16.dp)
            )
        }
        HorizontalDivider(color = AppColor.background)
    }
}
