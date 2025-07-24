package com.kosa.selp.features.gift.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.kosa.selp.features.gift.repository.GiftRepository
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyResultScreen(navController: NavController) {
    val gifts = remember { GiftRepository.getRecommendedGifts() }
    val visibleItems = remember { mutableStateListOf<Boolean>() }
    var showLottie by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("gift_open.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    var currentIndex by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()


    LaunchedEffect(currentIndex) {
        listState.animateScrollToItem(currentIndex)
    }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            delay(300)
            showLottie = false
            gifts.forEachIndexed { index, _ ->
                delay(150)
                visibleItems.add(true)
            }
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
        ) {
            if (showLottie) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(composition = composition, progress = { progress })
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "추천 선물 리스트",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColor.textPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

//                    GiftGrid(gifts = gifts, visibleItems = visibleItems)

                }
            }
        }
    }
}
