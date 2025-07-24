package com.kosa.selp.features.home.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosa.selp.R
import com.kosa.selp.shared.composable.navigation.BottomNavBar
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.Primary
import com.kosa.selp.shared.theme.Secondary
import com.kosa.selp.shared.theme.TextPrimary
import com.kosa.selp.shared.theme.TextSecondary
import com.kosa.selp.shared.theme.White

data class GiftItem(
    val title: String,
    val imageUrl: String,
    val price: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartSurvey: () -> Unit,
    onViewHistory: () -> Unit,
    onViewAnniversaries: () -> Unit,
    onViewSettings: () -> Unit
) {
    val anniversaries = listOf(
        "지민생일" to "D-7",
        "부모님 결혼기념일" to "D-10"
    )

    val recommendedGifts = listOf(
        GiftItem("향 좋은 디퓨저", "https://picsum.photos/id/1015/200/300", "19,800원"),
        GiftItem("레터링 케이크", "https://picsum.photos/id/1027/200/300", "35,000원"),
        GiftItem("무드등", "https://picsum.photos/id/1074/200/300", "28,900원"),
        GiftItem("감성 꽃다발", "https://picsum.photos/id/1080/200/300", "42,000원"),
        GiftItem("에어팟 케이스", "https://picsum.photos/id/1050/200/300", "12,500원"),
        GiftItem("감성 엽서 세트", "https://picsum.photos/id/1043/200/300", "8,900원"),
        GiftItem("LED 거울", "https://picsum.photos/id/1062/200/300", "32,000원"),
        GiftItem("무선 충전 패드", "https://picsum.photos/id/1033/200/300", "29,500원"),
        GiftItem("커플 머그잔", "https://picsum.photos/id/109/200/300", "22,000원"),
        GiftItem("목욕 바스켓 세트", "https://picsum.photos/id/1020/200/300", "38,000원")
    )

    Scaffold(
        containerColor = AppColor.white,
        bottomBar = {
            BottomNavBar(
                selectedIndex = 0,
                onItemSelected = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "어떤 선물을 해야할지\n고민하시는 보라님을 위해 준비했어요!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
            }

            Button(
                onClick = onStartSurvey,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(listOf(Primary, Secondary)),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("선물 추천받기", color = White, fontWeight = FontWeight.SemiBold)
                }
            }

            Column {
                Text("곧 다가올 날, 선물 준비하세요!", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(anniversaries) { (title, dday) ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.size(120.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = dday,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Secondary
                                    )
                                )
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }


            Column {
                Text("이런 선물은 어때요?", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(recommendedGifts) { gift ->Card(
                        modifier = Modifier
                            .width(180.dp)
                            .height(240.dp), // 적절히 늘림
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(gift.imageUrl)
                                    .crossfade(true)
                                    .listener(
                                        onStart = {
                                            Log.d("CoilImage", "이미지 로딩 시작: ${gift.imageUrl}")
                                        },
                                        onSuccess = { _, result ->
                                            Log.d("CoilImage", "이미지 로딩 성공: ${gift.imageUrl}")
                                        },
                                        onError = { _, error ->
                                            Log.e("CoilImage", "이미지 로딩 실패: ${gift.imageUrl}", error.throwable)
                                            Log.e("CoilImage", "에러 메시지: ${error.throwable.message}")
                                        }
                                    )
                                    .build(),
                                contentDescription = gift.title,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.selp_background),
                                error = painterResource(R.drawable.selp_background),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Text(
                                text = gift.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                maxLines = 1
                            )

                            Text(
                                text = gift.price,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    }
                }
            }
        }
    }
}
