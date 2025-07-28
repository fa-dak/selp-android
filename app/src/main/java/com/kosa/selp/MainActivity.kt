package com.kosa.selp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kosa.selp.features.calendar.presentation.CalendarScreen
import com.kosa.selp.features.calendar.presentation.EventRegisterScreen
import com.kosa.selp.features.gift.presentation.screen.AgeGroupGiftScreen
import com.kosa.selp.features.gift.presentation.screen.GiftDetailScreen
import com.kosa.selp.features.gift.presentation.screen.GiftPackageDetailScreen
import com.kosa.selp.features.home.presentation.screen.HomeScreen
import com.kosa.selp.features.login.presentation.screen.LoginScreen
import com.kosa.selp.features.login.presentation.viewModel.LoginEvent
import com.kosa.selp.features.login.presentation.viewModel.LoginViewModel
import com.kosa.selp.features.survey.presentation.screen.SurveyFunnelScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyIntroScreen
import com.kosa.selp.features.survey.presentation.screen.SurveyResultScreen
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.composable.navigation.BottomNavBar
import com.kosa.selp.shared.navigation.BottomBarRoute
import com.kosa.selp.shared.navigation.animatedComposable
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.SelpTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

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

            SelpTheme {
                Scaffold(
                    containerColor = AppColor.white,
                    bottomBar = {
                        if (BottomBarRoute.shouldShow(currentRoute)) {
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
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
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
                                navController = navController,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .consumeWindowInsets(innerPadding)
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

                        animatedComposable("surveyResult") { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry("surveyFunnel")
                            }
                            val viewModel = hiltViewModel<SurveyViewModel>(parentEntry)

                            SurveyResultScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }

                        composable("calendar") {
                            CalendarScreen(
                                navController = navController,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        composable(
                            route = "eventRegister/{selectedDateMillis}",
                            arguments = listOf(navArgument("selectedDateMillis") {
                                type = NavType.LongType
                            })
                        ) { backStackEntry ->
                            val selectedDateMillis =
                                backStackEntry.arguments?.getLong("selectedDateMillis")
                                    ?: System.currentTimeMillis()
                            val selectedDate = Date(selectedDateMillis)

                            EventRegisterScreen(
                                navController = navController,
                                selectedDate = selectedDate
                            )
                        }

                        animatedComposable("giftDetail/{giftId}") { backStackEntry ->
                            val giftId = backStackEntry.arguments?.getString("giftId")
                            if (giftId != null) {
                                GiftDetailScreen(giftId = giftId, navController = navController)
                            }
                        }

                        animatedComposable("giftPackage/{giftPackageId}") { backStackEntry ->
                            val giftPackageId = backStackEntry.arguments?.getString("giftPackageId")
                            if (giftPackageId != null) {
                                GiftPackageDetailScreen(
                                    giftPackageId = giftPackageId,
                                    navController = navController
                                )
                            }
                        }

                        composable("ageGift") {
                            AgeGroupGiftScreen(
                                navController = navController,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .consumeWindowInsets(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
