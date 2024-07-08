package com.fjr619.studyfocus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fjr619.studyfocus.presentation.NavGraphs
import com.fjr619.studyfocus.presentation.dashboard.DashboardScreen
import com.fjr619.studyfocus.presentation.session.SessionScreen
import com.fjr619.studyfocus.presentation.subject.SubjectScreen
import com.fjr619.studyfocus.presentation.task.TaskScreen
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun RootNavHost() {
    DestinationsNavHost(navGraph = NavGraphs.root)
}