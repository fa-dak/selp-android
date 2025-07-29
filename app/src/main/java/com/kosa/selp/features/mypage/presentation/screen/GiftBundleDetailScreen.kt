package com.kosa.selp.features.mypage.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageViewModel
import com.kosa.selp.shared.theme.AppColor
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBundleDetailScreen(
    bundleId: Long,
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    // ViewModel에서 상세 정보 상태를 구독
    val giftBundleDetail by viewModel.giftBundleDetail.collectAsStateWithLifecycle()

    // 화면이 시작될 때 API 호출
    LaunchedEffect(bundleId) {
        viewModel.fetchGiftBundleDetail(bundleId)
    }

    // 화면이 사라질 때 ViewModel의 상태를 초기화
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearGiftBundleDetail()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "선물 꾸러미 정보",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        containerColor = AppColor.background
    ) { innerPadding ->
        // 데이터 로딩 상태에 따라 다른 UI 표시
        if (giftBundleDetail == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val bundle = giftBundleDetail!!
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "'${bundle.receiverNickname ?: "-"}'님을 위한",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "'${bundle.eventName ?: "-"}' 꾸러미",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "포함된 선물 목록",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(bundle.products.size) { index ->
                    val product = bundle.products[index]
                    ProductItem(
                        product = product,
                        onClick = {
                            // detailPath가 비어있지 않을 때만 네비게이션 실행
                            if (product.detailPath.isNotBlank()) {
                                val encodedUrl = URLEncoder.encode(product.detailPath, "UTF-8")
                                navController.navigate("webView?url=$encodedUrl")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: GiftBundleResponse.Product,
    onClick: () -> Unit
) {
    val isClickable = product.detailPath.isNotBlank()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isClickable) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.white)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imagePath,
                contentDescription = product.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}