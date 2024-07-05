package com.fjr619.studyfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fjr619.studyfocus.presentation.dashboard.DashboardScreen
import com.fjr619.studyfocus.presentation.theme.StudyFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyFocusTheme {
                DashboardScreen()
            }
        }
    }
}
