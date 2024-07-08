package com.fjr619.studyfocus.navigation

import androidx.compose.runtime.Composable
import com.fjr619.studyfocus.presentation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun RootNavHost() {
    DestinationsNavHost(navGraph = NavGraphs.root)
}