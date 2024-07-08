package com.fjr619.studyfocus.domain.model

import androidx.compose.ui.graphics.Color
import com.fjr619.studyfocus.presentation.theme.gradient1
import com.fjr619.studyfocus.presentation.theme.gradient2
import com.fjr619.studyfocus.presentation.theme.gradient3
import com.fjr619.studyfocus.presentation.theme.gradient4
import com.fjr619.studyfocus.presentation.theme.gradient5

data class Subject(
    val name: String,
    val goalHours: Float,
    val colors: List<Color>,
    val subjectId: Int?
) {
    companion object {
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
