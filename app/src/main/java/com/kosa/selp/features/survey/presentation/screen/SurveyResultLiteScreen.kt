package com.kosa.selp.features.survey.presentation.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kosa.selp.features.survey.presentation.component.SurveyResultLottie
import com.kosa.selp.features.survey.presentation.composable.SurveyLiteResultContent
import com.kosa.selp.features.survey.presentation.viewModel.LiteSurveyViewModel
import com.kosa.selp.shared.composable.loading.DotsLoadingIndicator
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyResultLiteScreen(
    navController: NavController,
    viewModel: LiteSurveyViewModel,
    eventId: Long?
) {
    val recommendedGifts by viewModel.recommendedGiftBundles.collectAsStateWithLifecycle()
    var showLottie by remember { mutableStateOf(false) }
    var animationCompleted by remember { mutableStateOf(false) }
    var isInitialLoad by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("gift_open.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = showLottie && composition != null,
        restartOnPlay = false
    )

    LaunchedEffect(recommendedGifts, composition) {
        if (recommendedGifts != null && composition != null && isInitialLoad) {
            showLottie = true
        } else if (recommendedGifts != null && !isInitialLoad) {
            animationCompleted = true
        }
    }

    LaunchedEffect(progress, showLottie) {
        if (showLottie && progress == 1f) {
            delay(200)
            animationCompleted = true
            showLottie = false
            isInitialLoad = false
        }
    }

    val isLoading = recommendedGifts == null && isInitialLoad
    val isLottieReady =
        composition != null && recommendedGifts != null && showLottie && !animationCompleted && isInitialLoad
    val isContentReady = recommendedGifts != null && animationCompleted

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        if (isContentReady) {
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
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {})
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        DotsLoadingIndicator(
                            message = "선물을 추천 중이에요...\n잠시만 기다려주세요.",
                            textColor = AppColor.textPrimary
                        )
                    }
                }

                isLottieReady -> {
                    composition?.let { safeComposition ->
                        SurveyResultLottie(safeComposition, progress = progress)
                    }
                }

                isContentReady -> {
                    SurveyLiteResultContent(
                        gifts = recommendedGifts!!,
                        navController = navController,
                        viewModel = viewModel,
                        eventId = eventId
                    )
                }
            }
        }
    }
}