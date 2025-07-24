package com.kosa.selp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kosa.selp.features.calendar.presentation.CalendarScreen
import com.kosa.selp.features.gift.presentation.screen.SurveyResultScreen
import com.kosa.selp.features.home.presentation.screen.HomeScreen
import com.kosa.selp.features.login.presentation.screen.LoginScreen
import com.kosa.selp.features.login.presentation.viewModel.LoginEvent
import com.kosa.selp.features.login.presentation.viewModel.LoginViewModel
import com.kosa.selp.features.survey.presentation.screen.SurveyFunnelScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyIntroScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyResultWaitingScreen
import com.kosa.selp.features.survey.viewModel.SurveyViewModel
import com.kosa.selp.shared.composable.navigation.BottomNavBar
import com.kosa.selp.shared.navigation.BottomBarRoute
import com.kosa.selp.shared.navigation.animatedComposable
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.SelpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val currentBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry.value?.destination?.route
            val hasBottomBar = BottomBarRoute.shouldShow(currentRoute)
            val bottomPadding =
                if (hasBottomBar) 0.dp else WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding()

            SelpTheme {
                Scaffold(
                    containerColor = AppColor.white,
                    bottomBar = {
                        // 항상 동일한 높이의 bottomBar 공간 확보
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    56.dp + WindowInsets.navigationBars.asPaddingValues()
                                        .calculateBottomPadding()
                                ),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AnimatedVisibility(
                                visible = hasBottomBar,
                                enter = slideInVertically(
                                    initialOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(durationMillis = 250)
                                ),
                                exit = slideOutVertically(
                                    targetOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(durationMillis = 250)
                                )
                            ) {
                                BottomNavBar(
                                    selectedIndex = BottomBarRoute.indexOf(currentRoute),
                                    onItemSelected = { index ->
                                        val destination = BottomBarRoute.fromIndex(index)
                                        navController.navigate(destination) {
                                            popUpTo("home") { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(
                            bottom = innerPadding.calculateBottomPadding(),
                            top = innerPadding.calculateTopPadding(),
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                        )
                    ) {
                        composable("login") {
                            val viewModel: LoginViewModel = hiltViewModel()

                            LaunchedEffect(viewModel) {
                                viewModel.loginEvent.collect { event ->
                                    when (event) {
                                        is LoginEvent.LoginSuccess -> {
                                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT)
                                                .show()
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }

                                        is LoginEvent.LoginFailure -> {
                                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT)
                                                .show()
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

                        composable("home") {
                            HomeScreen(
                                navController = navController
                            )
                        }

                        animatedComposable("surveyIntro") {
                            SurveyIntroScreen(
                                navController = navController,
                            )
                        }

                        animatedComposable("surveyFunnel") {
                            SurveyFunnelScreen(
                                navController = navController,
                            )
                        }

                        animatedComposable("surveyResultLoading") { navBackStackEntry ->
                            val parentEntry = remember(navBackStackEntry) {
                                navController.getBackStackEntry("surveyFunnel")
                            }
                            val viewModel = hiltViewModel<SurveyViewModel>(parentEntry)

                            SurveyResultWaitingScreen(
                                navController = navController,
                                viewModel = viewModel,
                            )
                        }

                        animatedComposable("surveyResult") {
                            SurveyResultScreen(navController = navController)
                        }

                        composable("calendar") {
                            CalendarScreen()
                        }
                    }
                }
            }
        }
    }
}
