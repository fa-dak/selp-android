package com.kosa.selp.shared.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    enter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = {
        slideInHorizontally(initialOffsetX = { it }) + fadeIn()
    },
    exit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = {
        slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
    },
    popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = {
        slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
    },
    popExit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = {
        slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
    },
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enter,
        exitTransition = exit,
        popEnterTransition = popEnter,
        popExitTransition = popExit,
        content = content
    )
}