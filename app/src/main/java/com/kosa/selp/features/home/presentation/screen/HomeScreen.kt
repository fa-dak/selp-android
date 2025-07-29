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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val price: String
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

    when (state) {
        is HomeUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DotsLoadingIndicator(message = "불러오는 중...", textColor = AppColor.textPrimary)
            }
        }

        is HomeUiState.Error -> {
            val message = (state as HomeUiState.Error).message
            println("messagessage" + message)
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
                    createdAt = "", // 필요 시 포맷팅
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
    }
}


