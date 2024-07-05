package com.fjr619.studyfocus.presentation.dashboard.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardTopBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            text = "Study Focus",
            style = MaterialTheme.typography.headlineMedium
        )
    })
}