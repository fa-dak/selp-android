package com.kosa.selp.features.survey.presentation.screen

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kosa.selp.features.survey.presentation.component.SurveyResultLottie
import com.kosa.selp.features.survey.presentation.composable.SurveyResultContent
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.composable.loading.DotsLoadingIndicator
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyResultScreen(
    navController: NavController,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    val recommendedGifts by viewModel.recommendedGiftBundles.collectAsStateWithLifecycle()
    var showLottie by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("gift_open.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    LaunchedEffect(progress) {
        if (progress == 1f) {
            delay(300)
            showLottie = false
        }
    }

    val isLoading = recommendedGifts == null
    val isLottie = recommendedGifts != null && showLottie
    val isReady = recommendedGifts != null && !showLottie
    Log.i("SurveyViewModel", "surveyResultSccn: ${recommendedGifts}")

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

                isLottie -> {
                    val safeComposition = composition
                    if (safeComposition != null) {
                        SurveyResultLottie(safeComposition, progress = progress)
                    }
                }

                isReady -> SurveyResultContent(
                    gifts = recommendedGifts!!,
                    navController = navController
                )
            }
        }
    }
}


