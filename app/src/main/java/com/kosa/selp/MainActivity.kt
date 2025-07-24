package com.kosa.selp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kosa.selp.features.gift.presentation.screen.SurveyResultScreen
import com.kosa.selp.features.home.presentation.screen.HomeScreen
import com.kosa.selp.features.login.presentation.screen.LoginScreen
import com.kosa.selp.features.login.presentation.viewModel.LoginEvent
import com.kosa.selp.features.login.presentation.viewModel.LoginViewModel
import com.kosa.selp.features.survey.presentation.screen.SurveyFunnelScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyIntroScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyResultWaitingScreen
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
            val context = LocalContext.current

            SelpTheme {
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    animatedComposable("login") {
                        val viewModel: LoginViewModel = hiltViewModel()

                        LaunchedEffect(viewModel) {
                            viewModel.loginEvent.collect { event ->
                                when (event) {
                                    is LoginEvent.LoginSuccess -> {
                                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                    is LoginEvent.LoginFailure -> {
                                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        LoginScreen(
                            onKakaoLoginClick = {
                                viewModel.loginWithKakao(context)
                            }
                        )
                    }

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


