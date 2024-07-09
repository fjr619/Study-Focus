package com.fjr619.studyfocus.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.domain.model.Task

class DashboardContract {
    data class State(
        val totalSubjectCount: Int = 0,
        val totalStudiedHours: Float = 0f,
        val totalGoalStudyHours: Float = 0f,
        val subjects: List<Subject> = emptyList(),

        val newSubjectName: String = "",
        val newSubjectGoalStudyHours: String = "1",
        val newSubjectCardColors: List<Color> = Subject.subjectCardColors.random(),

        val session: Session? = null
    )

    sealed interface Action {
        data object ResetSubject : Action
        data object SaveSubject : Action
        data object DeleteSession : Action
        data class OnDeleteSessionButtonClick(val session: Session): Action
        data class OnTaskIsCompleteChange(val task: Task): Action
        data class OnSubjectCardColorChange(val colors: List<Color>): Action
        data class OnSubjectNameChange(val name: String): Action
        data class OnGoalStudyHoursChange(val hours: String): Action
    }
}