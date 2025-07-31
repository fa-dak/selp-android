package com.kosa.selp.features.home.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kosa.selp.features.home.composable.AnniversaryList
import com.kosa.selp.features.home.response.toGiftItem
import com.kosa.selp.features.home.viewModel.HomeUiState
import com.kosa.selp.features.home.viewModel.HomeViewModel
import com.kosa.selp.shared.composable.gift.GiftCardGrid
import com.kosa.selp.shared.composable.gift.GiftPackageRowList
import com.kosa.selp.shared.composable.loading.DotsLoadingIndicator
import com.kosa.selp.shared.theme.AppColor

data class GiftItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val detailPath: String,
    val price: Int
)

data class GiftPackage(
    val id: String,
    val title: String,
    val recipient: String,
    val createdAt: String,
    val gifts: List<GiftItem>
)

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.homeState.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        viewModel.loadUnreadCount()
    }

    when (state) {
        is HomeUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DotsLoadingIndicator(message = "불러오는 중...", textColor = AppColor.textPrimary)
            }
        }

        is HomeUiState.Error -> {
            val message = (state as HomeUiState.Error).message
            println("messagessage $message")
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "오류가 발생했습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is HomeUiState.Success -> {
            val data = (state as HomeUiState.Success).data
            val anniversaries = data.upcomingEvents.map { it.eventName to "D-${it.dday}" }
            val recommendedGifts = data.recommendProducts.map { it.toGiftItem() }
            val bundle = data.recentGiftBundleProducts

            val recentGiftPackages = bundle?.let {
                listOf(
                    GiftPackage(
                        id = it.giftBundleId.toString(),
                        title = "",
                        recipient = "",
                        createdAt = "",
                        gifts = it.products.map { it.toGiftItem() }
                    )
                )
            } ?: emptyList()

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(AppColor.white)
            ) {
                HeaderBar(navController, unreadCount)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColor.white)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = AppColor.primary.copy(alpha = 0.1f),
                                    spotColor = AppColor.primary.copy(alpha = 0.1f)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = AppColor.white)
                        ) {
                            Column() {
                                Row(
                                    verticalAlignment = Alignment.Top
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .background(
                                                brush = Brush.radialGradient(
                                                    listOf(
                                                        AppColor.primary.copy(alpha = 0.1f),
                                                        AppColor.secondary.copy(alpha = 0.05f)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(16.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "🎁",
                                            fontSize = 28.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "어떤 선물을 해야할지",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AppColor.textPrimary,
                                            lineHeight = 28.sp
                                        )
                                        Text(
                                            text = "고민이신가요?",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AppColor.textPrimary,
                                            lineHeight = 28.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "맞춤 추천을 받아보세요!",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = AppColor.textSecondary,
                                            lineHeight = 22.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = { navController.navigate("surveyIntro") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    contentPadding = PaddingValues(),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 4.dp,
                                        pressedElevation = 2.dp
                                    )
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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AutoAwesome,
                                                contentDescription = null,
                                                tint = AppColor.white,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                "선물 추천받기",
                                                color = AppColor.white,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item { AnniversaryList(anniversaries = anniversaries) }

                    item {
                        Text("촤근에 만든 선물꾸러미", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        GiftPackageRowList(
                            packages = recentGiftPackages,
                            onClick = { recentGiftPackage ->
                                if (recentGiftPackage.id.isNotBlank()) {
                                    navController.navigate("giftPackage/${recentGiftPackage.id}")
                                }
                            }
                        )
                    }

                    item {
                        Text("이런 선물은 어때요?", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        GiftCardGrid(items = recommendedGifts, navController = navController)
                    }
                }
            }
        }
    }
}
