package com.kosa.selp.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kosa.selp.features.home.composable.AnniversaryList
import com.kosa.selp.shared.composable.gift.GiftCardGrid
import com.kosa.selp.shared.composable.gift.GiftPackageRowList
import com.kosa.selp.shared.theme.AppColor

data class GiftItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val price: String
)

data class GiftPackage(
    val id: String,
    val title: String,
    val recipient: String,
    val createdAt: String,
    val gifts: List<GiftItem>
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
            "1",
            "향 좋은 디퓨저",
            "https://ldb-phinf.pstatic.net/20241226_124/1735176183750fjEhg_JPEG/%C8%A6%C5%D7%B5%F0%C4%C9%C0%CC%C5%A9_%281%29.jpg",
            "19,800원"
        ),
        GiftItem(
            "1",
            "레터링 케이크",
            "https://ldb-phinf.pstatic.net/20241226_81/1735176192690m2B0T_JPEG/%C8%AD%C0%CC%C6%AE%C8%A6%C5%D7%B5%F0.jpeg",
            "35,000원"
        ),
        GiftItem(
            "3",
            "무드등",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "28,900원"
        ),
        GiftItem(
            "4",
            "감성 꽃다발",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "42,000원"
        ),
        GiftItem(
            "3",
            "에어팟 케이스",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "12,500원"
        ),
        GiftItem(
            "2",
            "감성 엽서 세트",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "8,900원"
        ),
    )

    val recentGiftPackages = listOf(
        GiftPackage(
            id = "1",
            title = "20대 친구를 위한 선물",
            recipient = "친구",
            createdAt = "2025.07.26",
            gifts = recommendedGifts.take(4)
        ),
        GiftPackage(
            id = "2",
            title = "감사한 부모님 선물",
            recipient = "부모님",
            createdAt = "2025.07.20",
            gifts = recommendedGifts.shuffled().take(4)
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.white)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                shape = RoundedCornerShape(16.dp),
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
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("선물 추천받기", color = AppColor.white, fontWeight = FontWeight.SemiBold)
                }
            }
        }


        item { AnniversaryList(anniversaries = anniversaries) }
        item {
            Text("촤근에 만든 선물꾸러미", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            GiftPackageRowList(
                packages = recentGiftPackages,
                onClick = { giftPackage ->
                    navController.navigate("giftPackage/${giftPackage.id}")
                })
        }

        item {
            Text("이런 선물은 어때요?", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            GiftCardGrid(items = recommendedGifts, navController = navController)
        }


    }
}


