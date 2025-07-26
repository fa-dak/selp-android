package com.kosa.selp.features.gift.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kosa.selp.features.gift.composable.MessageRecommendBottomSheet
import com.kosa.selp.features.home.presentation.screen.GiftItem
import com.kosa.selp.shared.composable.gift.GiftCardGrid
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftPackageDetailScreen(
    giftPackageId: String,
    navController: NavController
) {
    val dummyGiftList = listOf(
        GiftItem(
            "1",
            "향 좋은 디퓨저",
            "https://ldb-phinf.pstatic.net/20241226_124/1735176183750fjEhg_JPEG/%C8%A6%C5%D7%B5%F0%C4%C9%C0%CC%C5%A9_%281%29.jpg",
            "19,800원"
        ),
        GiftItem(
            "2",
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
            "5",
            "에어팟 케이스",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "12,500원"
        ),
        GiftItem(
            "6",
            "감성 엽서 세트",
            "https://ldb-phinf.pstatic.net/20250416_9/1744784155310s0lbX_JPEG/%BE%F3%B1%D7%B7%B9%C0%CC_%C6%DB%C7%C7.jpeg",
            "8,900원"
        ),
    )

    var showSheet by remember { mutableStateOf(false) }

    BackHandler { navController.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        containerColor = AppColor.white,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = AppColor.primary,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Edit, "메시지 추천", tint = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        buildAnnotatedString {
                            append("보라님의 ")
                            withStyle(style = SpanStyle(color = AppColor.primary)) {
                                append("20대 친구를\n")
                            }
                            append("위한 추천 선물 결과입니다")
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AppColor.textPrimary
                    )
                }
            }
            item {
                GiftCardGrid(
                    items = dummyGiftList,
                    navController = navController,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (showSheet) {
            MessageRecommendBottomSheet(onDismiss = { showSheet = false })
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GiftPackageDetailPreview() {
    GiftPackageDetailScreen(
        giftPackageId = "dummy",
        navController = rememberNavController()
    )
}
