package com.kosa.selp.features.survey.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kosa.selp.features.survey.presentation.composable.ExitConfirmBottomSheet
import com.kosa.selp.features.survey.presentation.funnel.lite.AgeLiteFunnel
import com.kosa.selp.features.survey.presentation.funnel.lite.AnniversaryLiteFunnel
import com.kosa.selp.features.survey.presentation.funnel.lite.BudgetLiteFunnel
import com.kosa.selp.features.survey.presentation.funnel.lite.CategoryLiteFunnel
import com.kosa.selp.features.survey.presentation.funnel.lite.GenderLiteFunnel
import com.kosa.selp.features.survey.presentation.funnel.lite.RelationshipLiteFunnel
import com.kosa.selp.features.survey.presentation.state.LiteSurveyEvent
import com.kosa.selp.features.survey.presentation.state.SurveyProgress
import com.kosa.selp.features.survey.presentation.state.SurveyStep
import com.kosa.selp.features.survey.presentation.viewModel.LiteSurveyViewModel
import com.kosa.selp.shared.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SurveyFunnelLiteScreen(
    navController: NavController,
    contactId: Long,
    anniversary: String? = null,
    viewModel: LiteSurveyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val exitSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showExitSheet by remember { mutableStateOf(false) }

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Text(text = "데이터 불러오는 중...", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    if (state.error != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Text(text = "오류 발생: ${state.error}", color = AppColor.primary)
        }
        return
    }

    if (showExitSheet) {
        ExitConfirmBottomSheet(
            sheetState = exitSheetState,
            onDismiss = { showExitSheet = false },
            onConfirm = {
                showExitSheet = false
                navController.popBackStack("home", inclusive = false)
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                LinearProgressIndicator(
                    progress = { SurveyProgress.getRatio(state.step) },
                    modifier = Modifier.fillMaxWidth(),
                    color = AppColor.primary
                )
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            if (state.step == SurveyStep.BUDGET) {
                                showExitSheet = true
                            } else {
                                viewModel.onEvent(LiteSurveyEvent.BackClicked)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "뒤로가기",
                                tint = AppColor.textPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showExitSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "닫기",
                                tint = AppColor.textSecondary
                            )
                        }
                    },
                    title = {},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColor.white
                    )
                )
            }
        },
        containerColor = AppColor.white
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = state.step,
                transitionSpec = {
                    val direction = if (targetState.ordinal > initialState.ordinal) 1 else -1
                    slideInHorizontally(
                        initialOffsetX = { width -> width * direction },
                        animationSpec = tween(durationMillis = 300)
                    ).togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { width -> -width * direction },
                            animationSpec = tween(durationMillis = 300)
                        )
                    )
                },
                label = "StepAnimation"
            ) { step ->
                when (step) {
                    SurveyStep.BUDGET -> BudgetLiteFunnel(
                        onNext = { viewModel.onEvent(LiteSurveyEvent.NextClicked) }
                    )

                    SurveyStep.AGE -> AgeLiteFunnel(
                        onNext = { viewModel.onEvent(LiteSurveyEvent.NextClicked) }
                    )

                    SurveyStep.GENDER -> GenderLiteFunnel(
                        onNext = { viewModel.onEvent(LiteSurveyEvent.NextClicked) }
                    )

                    SurveyStep.RELATIONSHIP -> RelationshipLiteFunnel(
                        onNext = { viewModel.onEvent(LiteSurveyEvent.NextClicked) }
                    )

                    SurveyStep.CATEGORY -> CategoryLiteFunnel(
                        onNext = { viewModel.onEvent(LiteSurveyEvent.NextClicked) }
                    )

                    SurveyStep.ANNIVERSARY -> AnniversaryLiteFunnel(
                        onNext = { viewModel.onEvent(LiteSurveyEvent.NextClicked) }
                    )

                    SurveyStep.COMPLETE -> {
                        LaunchedEffect(Unit) {
                            viewModel.onEvent(LiteSurveyEvent.SubmitClicked)
                            navController.navigate("surveyResultLite?contactId=${viewModel.contactId}") {
                                popUpTo("surveyFunnelLite") { inclusive = false }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
