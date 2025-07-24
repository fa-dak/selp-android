package com.kosa.selp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kosa.selp.features.gift.presentation.screen.SurveyResultScreen
import com.kosa.selp.features.home.presentation.screen.HomeScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyResultWaitingScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyFunnelScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyIntroScreen
import com.kosa.selp.features.survey.viewModel.SurveyViewModel
import com.kosa.selp.shared.navigation.animatedComposable
import com.kosa.selp.shared.theme.SelpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController();

            SelpTheme {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    animatedComposable("home") {
                        HomeScreen(
                            onStartSurvey = { navController.navigate("surveyIntro") },
                            onViewHistory = {},
                            onViewAnniversaries = {},
                            onViewSettings = {}
                        )
                    }

                    animatedComposable("surveyIntro") {
                        SurveyIntroScreen(
                            onStartSurvey = {
                                navController.navigate("surveyFunnel")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    animatedComposable("surveyFunnel") {
                        SurveyFunnelScreen(
                            navController = navController,
                            onExit = {
                                navController.popBackStack("home", inclusive = false)
                            }
                        )
                    }

                    animatedComposable("surveyResultLoading") { navBackStackEntry ->
                        val parentEntry = remember(navBackStackEntry) {
                            navController.getBackStackEntry("surveyFunnel")
                        }
                        val viewModel = hiltViewModel<SurveyViewModel>(parentEntry)

                        SurveyResultWaitingScreen(
                            viewModel = viewModel,
                            onComplete = { navController.navigate("surveyResult") }
                        )
                    }

                    animatedComposable("surveyResult") {
                        SurveyResultScreen(navController = navController)
                    }
                }

            }
        }
    }
}

