package com.fjr619.studyfocus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fjr619.studyfocus.presentation.dashboard.DashboardScreen
import com.fjr619.studyfocus.presentation.subject.SubjectScreen
import com.fjr619.studyfocus.presentation.task.TaskScreen

@Composable
fun RootNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "task") {
        composable("dashboard") {
            DashboardScreen()
        }

        composable("subject") {
            SubjectScreen()
        }

        composable("task") {
            TaskScreen()
        }
    }
}