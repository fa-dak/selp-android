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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kosa.selp.features.survey.presentation.composable.ExitConfirmBottomSheet
import com.kosa.selp.features.survey.presentation.composable.FreeInputBottomSheet
import com.kosa.selp.features.survey.presentation.funnel.AgeFunnel
import com.kosa.selp.features.survey.presentation.funnel.AnniversaryFunnel
import com.kosa.selp.features.survey.presentation.funnel.BudgetFunnel
import com.kosa.selp.features.survey.presentation.funnel.CategoryFunnel
import com.kosa.selp.features.survey.presentation.funnel.GenderFunnel
import com.kosa.selp.features.survey.presentation.funnel.RelationshipFunnel
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.state.SurveyProgress
import com.kosa.selp.features.survey.presentation.state.SurveyStep
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SurveyFunnelScreen(
    navController: NavController,
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
                navController.popBackStack("home", inclusive = false)
            }
        )
    }

    if (showFreeInputSheet) {
        FreeInputBottomSheet(
            sheetState = inputSheetState,
            onDismiss = { showFreeInputSheet = false },
            onSubmit = { input ->
                viewModel.onEvent(SurveyEvent.UserMessageEntered(input))
                coroutineScope.launch { inputSheetState.hide() }
                viewModel.onEvent(SurveyEvent.SubmitClicked) // COMPLETED로변경ㄴ하고 API 요청
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

                    SurveyStep.ANNIVERSARY -> AnniversaryFunnel(onNext = {
                        viewModel.onEvent(SurveyEvent.NextClicked)
                    })
                    
                    SurveyStep.RELATIONSHIP -> RelationshipFunnel(onNext = {
                        viewModel.onEvent(SurveyEvent.NextClicked)
                    })


                    SurveyStep.CATEGORY -> CategoryFunnel(onNext = {
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

                    SurveyStep.COMPLETE -> {
                        LaunchedEffect(Unit) {
                            navController.navigate("surveyResult") {
                                popUpTo("surveyFunnel") { inclusive = false }
                            }
                        }
                    }
                }
            }
        }
    }
}