package com.kosa.selp.features.survey.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kosa.selp.features.survey.presentation.components.*
import com.kosa.selp.features.survey.presentation.funnel.*
import com.kosa.selp.features.survey.viewModel.*
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SurveyFunnelScreen(
    navController: NavController,
    onExit: () -> Unit,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val exitSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showExitSheet by remember { mutableStateOf(false) }

    val inputSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFreeInputSheet by remember { mutableStateOf(false) }

    if (showExitSheet) {
        ExitConfirmBottomSheet(
            sheetState = exitSheetState,
            onDismiss = { showExitSheet = false },
            onConfirm = {
                showExitSheet = false
                onExit()
            }
        )
    }

    if (showFreeInputSheet) {
        FreeInputBottomSheet(
            sheetState = inputSheetState,
            onDismiss = { showFreeInputSheet = false },
            onSubmit = { input ->
                viewModel.onEvent(SurveyEvent.FreeInputEntered(input))
                viewModel.onEvent(SurveyEvent.SubmitClicked)
                coroutineScope.launch { inputSheetState.hide() }
                navController.navigate("surveyResultLoading")
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
                            if (state.step == SurveyStep.BUDGET) showExitSheet = true
                            else viewModel.onEvent(SurveyEvent.BackClicked)
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
                    SurveyStep.BUDGET -> BudgetFunnel(onNext = {
                        viewModel.onEvent(SurveyEvent.NextClicked)
                    })

                    SurveyStep.RELATIONSHIP -> RelationshipFunnel(onNext = {
                        viewModel.onEvent(SurveyEvent.NextClicked)
                    })

                    SurveyStep.PREFERENCE -> PreferenceFunnel(onNext = {
                        viewModel.onEvent(SurveyEvent.NextClicked)
                    })

                    SurveyStep.AGE -> AgeFunnel(onNext = {
                        viewModel.onEvent(SurveyEvent.NextClicked)
                    })

                    SurveyStep.GENDER -> GenderFunnel(
                        onRequestFreeInput = {
                            showFreeInputSheet = true
                        }
                    )

                    SurveyStep.COMPLETE -> TODO()
                }
            }
        }
    }
}