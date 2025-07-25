package com.kosa.selp.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosa.selp.R
import com.kosa.selp.features.home.ageGroupGift.screen.AgeGroupGiftSection
import com.kosa.selp.shared.theme.AppColor

data class GiftItem(
    val title: String,
    val imageUrl: String,
    val price: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val anniversaries = listOf(
        "친구 생일" to "D-7",
        "부모님 결혼기념일" to "D-10"
    )

    val recommendedGifts = listOf(
        GiftItem(
            "향 좋은 디퓨저",
            "https://ldb-phinf.pstatic.net/20241226_124/1735176183750fjEhg_JPEG/%C8%A6%C5%D7%B5%F0%C4%C9%C0%CC%C5%A9_%281%29.jpg",
            "19,800원"
        ),
        GiftItem(
            "레터링 케이크",
            "https://ldb-phinf.pstatic.net/20241226_81/1735176192690m2B0T_JPEG/%C8%AD%C0%CC%C6%AE%C8%A6%C5%D7%B5%F0.jpeg",
            "35,000원"
        ),
        GiftItem(
            "무드등",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "28,900원"
        ),
        GiftItem(
            "감성 꽃다발",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "42,000원"
        ),
        GiftItem(
            "에어팟 케이스",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "12,500원"
        ),
        GiftItem(
            "감성 엽서 세트",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "8,900원"
        ),
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.white)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
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
        }

        item {
            Button(
                onClick = { navController.navigate("surveyIntro") },
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
                            brush = Brush.horizontalGradient(
                                listOf(AppColor.primary, AppColor.secondary)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("선물 추천받기", color = AppColor.white, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(anniversaries) { (title, dayName) ->
                    Card(
                        modifier = Modifier
                            .width(130.dp)
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            AppColor.primary,
                                            AppColor.secondary.copy(alpha = 0.8f)
                                        )
                                    )
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                        .background(
                                            color = Color.White.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                        ),
                                        color = Color.White
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp),
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White.copy(alpha = 0.9f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        item {
            Text("이런 선물은 어때요?", fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
        }

        item {
            recommendedGifts.chunked(2).forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { gift ->
                        GiftCard(
                            gift = gift,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            AgeGroupGiftSection()
        }
    }
}

@Composable
fun GiftCard(
    gift: GiftItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(gift.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = gift.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.selp_background),
                error = painterResource(R.drawable.selp_background),
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = gift.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = AppColor.textSecondary,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1
        )

        Text(
            text = gift.price,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = AppColor.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

