package com.kosa.selp.features.gift.presentation.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.gift.composable.GiftDetailWebView
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GiftDetailScreen(
    giftId: String,
    navController: NavController
) {
    val giftUrlMap = mapOf(
        "1" to "https://www.thehyundai.com/front/pda/itemPtc.thd?slitmCd=40A1928964",
        "2" to "https://www.thehyundai.com/front/pda/itemPtc.thd?slitmCd=40A1928999",
        "3" to "https://www.thehyundai.com/front/pda/itemPtc.thd?slitmCd=40A1928031"
    )

    val url = giftUrlMap[giftId]
    var isLoading by remember { mutableStateOf(true) }
    val webViewAlpha by remember { derivedStateOf { if (isLoading) 0f else 1f } }

    BackHandler { navController.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = TextPrimary
                        )
                    }
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        containerColor = AppColor.white
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (url != null) {
                GiftDetailWebView(
                    url = url,
                    onLoadingFinished = { isLoading = false },
                    alpha = webViewAlpha,
                    modifier = Modifier.fillMaxSize()
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(color = AppColor.primary)
                    }
                }

            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "경고",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "유효하지 않은 선물입니다.",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColor.textPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "상품 정보를 다시 확인해주세요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColor.textSecondary
                    )
                }
            }
        }
    }
}
