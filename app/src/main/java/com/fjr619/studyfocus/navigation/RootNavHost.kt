package com.fjr619.studyfocus.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import com.fjr619.studyfocus.presentation.NavGraphs
import com.fjr619.studyfocus.presentation.destinations.SessionScreenDestination
import com.fjr619.studyfocus.presentation.session.timer_service.SessionTimerService
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine

@Composable
fun RootNavHost(timerService: SessionTimerService) {
    val navController = rememberNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
//            enterTransition = {  slideIntoContainer(
//                AnimatedContentTransitionScope.SlideDirection.Left,
//                animationSpec = tween(700)
//            ) },
//            exitTransition = { slideOutOfContainer(
//                AnimatedContentTransitionScope.SlideDirection.Left,
//                animationSpec = tween(700)
//            ) },
//            popEnterTransition = {
//                slideIntoContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Right,
//                    animationSpec = tween(700)
//                )
//            },
//            popExitTransition = {
//                slideOutOfContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Right,
//                    animationSpec = tween(700)
//                )
//            }
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 2 },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 2 },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            }
        ))
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        engine = navController,
        dependenciesContainerBuilder = {
            dependency(SessionScreenDestination) {
                timerService
            }
        }
    )
}