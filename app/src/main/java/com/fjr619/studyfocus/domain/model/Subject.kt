package com.fjr619.studyfocus.domain.model

import androidx.compose.ui.graphics.Color
import com.fjr619.studyfocus.presentation.theme.gradient1
import com.fjr619.studyfocus.presentation.theme.gradient2
import com.fjr619.studyfocus.presentation.theme.gradient3
import com.fjr619.studyfocus.presentation.theme.gradient4
import com.fjr619.studyfocus.presentation.theme.gradient5

data class Subject(
    val name: String = "",
    val goalHours: Float = 1f,
    val colors: List<Color> = subjectCardColors.random(),
    val subjectId: Int? = null
) {
    companion object {
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
