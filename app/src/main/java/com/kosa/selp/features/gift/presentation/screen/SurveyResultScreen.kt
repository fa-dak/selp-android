package com.kosa.selp.features.gift.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kosa.selp.features.gift.model.Gift
import com.kosa.selp.features.gift.repository.GiftRepository
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.delay
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyResultScreen(navController: NavController) {
    val gifts = remember { GiftRepository.getRecommendedGifts() }
    var showLottie by remember { mutableStateOf(true) }
    var currentIndex by remember { mutableStateOf(0) }
    val currentGift = gifts.getOrNull(currentIndex)

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("gift_open.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    LaunchedEffect(progress) {
        if (progress == 1f) {
            delay(300)
            showLottie = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        if (!showLottie) {
                            navController.navigate("home") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = AppColor.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        containerColor = AppColor.white
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            if (showLottie) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(composition = composition, progress = { progress })
                }
            } else if (currentGift != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("보라님의 ")
                            withStyle(style = SpanStyle(color = AppColor.primary)) {
                                append("20대 친구를\n")
                            }
                            append("위한 추천 선물 결과입니다")
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AppColor.textPrimary,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(24.dp))


                    GiftCarouselMultiBrowse(gifts)

                    Spacer(modifier = Modifier.weight(1f))


                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColor.primary)
                    ) {
                        Text(
                            "이렇게 결정!",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftCarouselMultiBrowse(gifts: List<Gift>) {
    val windowSize = LocalWindowInfo.current.containerSize
    val screenWidth = with(LocalDensity.current) { windowSize.width.toDp() }
    val screenHeight = with(LocalDensity.current) { windowSize.height.toDp() }

    val cardWidth = screenWidth * 0.72f
    val cardHeight = screenHeight * 0.33f

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { gifts.size },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp),
        preferredItemWidth = cardWidth,
        itemSpacing = 12.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { index ->
        val gift = gifts[index]

        Box(
            modifier = Modifier
                .height(cardHeight)
                .maskClip(MaterialTheme.shapes.extraLarge)
        ) {
            Image(
                painter = rememberAsyncImagePainter(gift.imageUrl),
                contentDescription = gift.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            AssistChip(
                onClick = {},
                label = {
                    Text(
                        "재추천",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColor.white
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, AppColor.primary),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = AppColor.primary,
                    labelColor = Color.White
                )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xCC000000))
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = gift.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1
                )
                Text(
                    text = "${DecimalFormat("#,###").format(gift.price)}원",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f))
                )
            }
        }
    }
}


