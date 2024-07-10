package com.fjr619.studyfocus.presentation.session

import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.presentation.session.timer_service.TimerState

class SessionContract {

    data class State(
        val subjects: List<Subject> = emptyList(),
        val sessions: List<Session> = emptyList(),
        val relatedToSubject: String? = null,
        val subjectId: Int? = null,
        val session: Session? = null,

        val hours: String = "00",
        val minutes: String = "00",
        val seconds: String = "00",
        val currentTimerState: TimerState = TimerState.IDLE
    )

    sealed interface Action {
        data class OnRelatedSubjectChange(val subject: Subject) : Action
        data class OnDeleteSessionButtonClick(val session: Session) : Action
        data object NotifyToUpdateSubject : Action
        data class UpdateSubjectIdAndRelatedSubject(
            val subjectId: Int?,
            val relatedToSubject: String?
        ) : Action

        data object StartSession: Action
        data object StopSession: Action
        data object FinishSession: Action
        data class SaveSession(val duration: Long) : Action
        data object DeleteSession : Action

    }
}