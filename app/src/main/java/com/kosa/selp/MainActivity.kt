package com.kosa.selp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iamport.sdk.domain.core.Iamport
import com.kosa.selp.features.calendar.presentation.CalendarScreen
import com.kosa.selp.features.calendar.presentation.EventRegisterScreen
import com.kosa.selp.features.gift.presentation.screen.AgeGroupGiftScreen
import com.kosa.selp.features.gift.presentation.screen.GiftDetailScreen
import com.kosa.selp.features.gift.presentation.screen.GiftPackageDetailScreen
import com.kosa.selp.features.gift.presentation.screen.SurveyResultScreen
import com.kosa.selp.features.home.presentation.screen.HomeScreen
import com.kosa.selp.features.login.presentation.screen.LoginScreen
import com.kosa.selp.features.login.presentation.viewModel.LoginEvent
import com.kosa.selp.features.login.presentation.viewModel.LoginViewModel
import com.kosa.selp.features.mypage.presentation.screen.GiftBundleDetailScreen
import com.kosa.selp.features.mypage.presentation.screen.GiftBundleListScreen
import com.kosa.selp.features.mypage.presentation.screen.MyContactsScreen
import com.kosa.selp.features.mypage.presentation.screen.MyPageScreen
import com.kosa.selp.features.pay.PayExampleScreen
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
import java.net.URLDecoder
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
            val loginViewModel: LoginViewModel = hiltViewModel()

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
                        startDestination = "splash",
                    ) {
                        composable("splash") {
                            SplashScreen(
                                viewModel = loginViewModel,
                                onNavigateToHome = {
                                    navController.navigate("home") {
                                        popUpTo("splash") {
                                            inclusive = true
                                        }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo("splash") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable("login") {
                            LaunchedEffect(loginViewModel) {
                                loginViewModel.loginEvent.collect { event ->
                                    when (event) {
                                        is LoginEvent.LoginSuccess -> {
                                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT)
                                                .show()
                                            navController.navigate("home") {
                                                popUpTo("login") {
                                                    inclusive = true
                                                }
                                            }
                                        }

                                        is LoginEvent.LoginFailure -> {
                                            Toast.makeText(
                                                context,
                                                "로그인 실패: ${event.error.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }

                            LoginScreen(
                                onKakaoLoginClick = {
                                    loginViewModel.loginWithKakao(context)
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
                            SurveyIntroScreen(navController = navController)
                        }

                        animatedComposable("surveyFunnel") {
                            SurveyFunnelScreen(navController = navController)
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

                        animatedComposable(
                            "webView?url={url}",
                            arguments = listOf(navArgument("url") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { backStackEntry ->
                            val encodedUrl = backStackEntry.arguments?.getString("url")
                            val url = if (encodedUrl != null) URLDecoder.decode(
                                encodedUrl,
                                "UTF-8"
                            ) else null
                            GiftDetailScreen(url = url, navController = navController)
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

                        // --- 마이페이지 관련 네비게이션 ---
                        composable("mypage") {
                            MyPageScreen(
                                navController = navController,
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) // 모든 백스택 제거
                                    }
                                },
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .consumeWindowInsets(innerPadding)
                            )
                        }

                        composable("payTest") {
                            PayExampleScreen()
                        }

                        animatedComposable("giftBundleList") {
                            GiftBundleListScreen(navController = navController)
                        }
                        animatedComposable("myContacts") {
                            MyContactsScreen()
                        }
                        animatedComposable("giftBundleDetail/{bundleId}") { backStackEntry ->
                            val bundleId =
                                backStackEntry.arguments?.getString("bundleId")?.toLongOrNull()
                            if (bundleId != null) {
                                GiftBundleDetailScreen(
                                    bundleId = bundleId,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
        Iamport.init(this) // Iamport
    }

    @Composable
    fun SplashScreen(
        viewModel: LoginViewModel,
        onNavigateToHome: () -> Unit,
        onNavigateToLogin: () -> Unit
    ) {
        val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
        LaunchedEffect(isLoggedIn) {
            when (isLoggedIn) {
                true -> onNavigateToHome()
                false -> onNavigateToLogin()
                null -> { /* Wait */
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Iamport.close()
    }
}