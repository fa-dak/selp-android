package com.kosa.selp.features.survey.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kosa.selp.features.survey.viewModel.SurveyStep
import com.kosa.selp.features.survey.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun SurveyResultWaitingScreen(
    navController: NavController,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.step) {
        snapshotFlow { state.step }
            .distinctUntilChanged()
            .filter { it == SurveyStep.COMPLETE }
            .collectLatest {
                delay(300)
                navController.navigate("surveyResult")
            }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppColor.primary)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "선물을 추천 중이에요...\n잠시만 기다려주세요.",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColor.textPrimary
            )
        }
    }
}
