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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageViewModel
import com.kosa.selp.features.pay.PayStatus
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.SelpTheme
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBundleListScreen(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val groupedBundles by viewModel.giftBundles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMyGiftBundles()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "내 선물 꾸러미",
                        style = MaterialTheme.typography.titleMedium, // 스타일 적용
                        fontWeight = FontWeight.Bold // FontWeight 적용
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            // groupedBundles의 key(날짜)들을 순회
            val dates = groupedBundles.keys.sortedDescending()
            dates.forEachIndexed { index, date ->
                // 1. 날짜 헤더 표시
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp, top = if (index > 0) 24.dp else 0.dp)
                    )
                }
                // 2. 해당 날짜의 꾸러미 카드 목록 표시
                val bundlesOnDate = groupedBundles[date] ?: emptyList()
                items(bundlesOnDate) { bundle ->
                    GiftBundleItem(
                        bundle = bundle,
                        onClick = {
                            navController.navigate("giftBundleDetail/${bundle.giftBundleId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp)) // 카드 사이의 간격
                }
            }
        }
    }
}

@Composable
fun GiftBundleItem(
    bundle: GiftBundleResponse,
    onClick: () -> Unit
) {
    val totalPrice = bundle.products.sumOf { it.price }
    val formatter = DecimalFormat("#,###")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // 클릭 이벤트 추가
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.white)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                // 받는 사람 정보
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "받는 사람",
                        tint = AppColor.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = bundle.receiverNickname ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${bundle.relationship}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColor.textDisabled
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // 이벤트 정보
                Text(
                    text = "${bundle.eventName ?: "-"}  ${bundle.eventDate}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 상품 이미지 목록
                Text(
                    text = "담은 선물",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bundle.products) { product ->
                        AsyncImage(
                            model = product.imagePath,
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "총 ${formatter.format(totalPrice)}원",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 결제 상태 정보
                val statusText = when (bundle.currentPayStatus) {
                    PayStatus.NOT_STARTED -> "미결제"
                    PayStatus.PAID -> "결제 완료"
                    PayStatus.CANCEL -> "결제 취소"
                }
                val statusColor = when (bundle.currentPayStatus) {
                    PayStatus.NOT_STARTED -> AppColor.textDisabled
                    PayStatus.PAID -> AppColor.primary
                    PayStatus.CANCEL -> AppColor.error
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GiftBundleItemPreview() {
    val fakeProduct = GiftBundleResponse.Product(
        productId = 1, category = "BEAUTY", name = "딥티크 핸드앤바디 젤", price = 84000,
        imagePath = "https://image.thehyundai.com/static/6/9/8/92/A1/40A1928964_0_600.jpg", detailPath = ""
    )
    val fakeBundle = GiftBundleResponse(
        giftBundleId = 1,
        createdDate = "25.07.27",
        eventId = 1,
        eventType = "BIRTHDAY",
        eventDate = "2025-08-15",
        eventName = "민준이 생일",
        receiverInfoId = 1,
        receiverNickname = "대학 동기(김민준)",
        relationship = "친구",
        products = listOf(fakeProduct, fakeProduct, fakeProduct),
        PayStatus.NOT_STARTED
    )
    SelpTheme {
        GiftBundleItem(
            bundle = fakeBundle,
            onClick = {} // Preview에서는 아무 동작도 하지 않는 빈 람다 전달
        )
    }
}
