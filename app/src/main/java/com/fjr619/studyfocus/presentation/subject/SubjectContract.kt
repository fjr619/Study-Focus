package com.fjr619.studyfocus.presentation.subject

import androidx.compose.ui.graphics.Color
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.domain.model.Task

class SubjectContract {
    data class State(
        val currentSubjectId: Int? = null,
        val subjectName: String = "",
        val goalStudyHours: String = "",
        val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
        val studiedHours: Float = 0f,
        val progress: Float = 0f,
        val recentSessions: List<Session> = emptyList(),
        val upcomingTasks: List<Task> = emptyList(),
        val completedTasks: List<Task> = emptyList(),
        val session: Session? = null
    )

    sealed interface Action {
        data object UpdateSubject : Action
        data object DeleteSubject : Action
        data object DeleteSession : Action
        data object UpdateProgress : Action
        data object ResetSubject: Action
        data class OnTaskIsCompleteChange(val task: Task): Action
        data class OnSubjectCardColorChange(val color: List<Color>): Action
        data class OnSubjectNameChange(val name: String): Action
        data class OnGoalStudyHoursChange(val hours: String): Action
        data class OnDeleteSessionButtonClick(val session: Session): Action
    }

    sealed interface Event {
        data object NavigateBack : Event
    }
}