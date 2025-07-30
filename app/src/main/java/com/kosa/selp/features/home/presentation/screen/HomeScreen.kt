package com.kosa.selp.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

            val recentGiftPackages = listOf(
                GiftPackage(
                    id = bundle?.giftBundleId?.toString() ?: "",
                    title = "최근에 만든 선물꾸러미",
                    recipient = "",
                    createdAt = "",
                    gifts = bundle?.products?.map { it.toGiftItem() } ?: emptyList()
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

                        IconButton(onClick = {
                            navController.navigate("notification")
                        }) {
                            Box {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "알림 보기"
                                )
                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 2.dp, y = (-2).dp)
                                            .background(Color.Red, shape = RoundedCornerShape(50))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
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
                                Text(
                                    "선물 추천받기",
                                    color = AppColor.white,
                                    fontWeight = FontWeight.SemiBold
                                )
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
