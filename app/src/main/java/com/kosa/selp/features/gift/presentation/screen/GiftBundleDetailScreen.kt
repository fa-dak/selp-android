package com.kosa.selp.features.gift.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kosa.selp.features.gift.composable.bundle.GiftBundleContent
import com.kosa.selp.features.gift.composable.bundle.GiftBundleDetailSkeleton
import com.kosa.selp.features.gift.composable.bundle.GiftBundleScaffold
import com.kosa.selp.features.gift.composable.bundle.MessageOverlay
import com.kosa.selp.features.gift.composable.bundle.MessageRecommendBottomSheet
import com.kosa.selp.features.gift.presentation.viewModel.GiftBundleDataViewModel
import com.kosa.selp.features.gift.presentation.viewModel.GiftBundleRecommendMessageUiViewModel
import com.kosa.selp.features.gift.presentation.viewModel.GiftBundleUiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBundleDetailScreen(
    giftPackageId: String,
    navController: NavController,
    uiViewModel: GiftBundleUiViewModel = hiltViewModel(),
    dataViewModel: GiftBundleDataViewModel = hiltViewModel(),
    messageViewModel: GiftBundleRecommendMessageUiViewModel = hiltViewModel(),
) {
    val uiState by uiViewModel.collectUiState().collectAsState()
    val giftBundleData by dataViewModel.giftBundleData.collectAsState()
    val recommendedMessages by dataViewModel.recommendedMessages.collectAsState()

    LaunchedEffect(giftPackageId) {
        dataViewModel.loadGiftBundle(giftPackageId)
    }

    when (val data = giftBundleData) {
        null -> GiftBundleDetailSkeleton(onBack = { navController.popBackStack() })
        else -> {
            GiftBundleScaffold(
                onBack = { navController.popBackStack() },
                onFabClick = { uiViewModel.showBottomSheet() },
                giftBundle = data
            ) { innerPadding ->
                GiftBundleContent(
                    giftBundle = data,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }


    if (uiState.showBottomSheet) {
        MessageRecommendBottomSheet(
            viewModel = messageViewModel,
            onDismiss = { uiViewModel.dismissBottomSheet() },
            onComplete = {
                messageViewModel.selectedTone.value?.let { tone ->
                    val finalTone =
                        if (tone == "직접 입력할게요") messageViewModel.customMessage.value else tone
                    dataViewModel.loadRecommendedMessages("20", finalTone)
                    uiViewModel.showOverlay()
                }
            }
        )
    }

    if (uiState.showOverlay) {
        MessageOverlay(
            messages = recommendedMessages,
            isLoading = recommendedMessages.isEmpty(),
            onDismiss = {
                uiViewModel.resetOverlay()
                messageViewModel.reset()
                dataViewModel.resetMessages()
            }
        )
    }
}
