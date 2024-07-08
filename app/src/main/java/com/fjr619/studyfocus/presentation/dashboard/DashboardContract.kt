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

    sealed interface Event {
        data object ResetSubject : Event
        data object SaveSubject : Event
        data object DeleteSession : Event
        data class OnDeleteSessionButtonClick(val session: Session): Event
        data class OnTaskIsCompleteChange(val task: Task): Event
        data class OnSubjectCardColorChange(val colors: List<Color>): Event
        data class OnSubjectNameChange(val name: String): Event
        data class OnGoalStudyHoursChange(val hours: String): Event
    }
}