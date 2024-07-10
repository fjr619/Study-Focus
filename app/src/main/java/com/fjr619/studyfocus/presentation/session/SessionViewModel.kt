package com.fjr619.studyfocus.presentation.session

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.repository.SessionRepository
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import com.fjr619.studyfocus.presentation.session.timer_service.TimerState
import com.fjr619.studyfocus.presentation.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class SessionViewModel(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
): ViewModel() {

    private val _state = MutableStateFlow(SessionContract.State())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects(),
        sessionRepository.getAllSessions()
    ) { state, subjects, sessions ->
        state.copy(
            subjects = subjects,
            sessions = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SessionContract.State()
    )

    fun onAction(action: SessionContract.Action) {
        when (action) {
            SessionContract.Action.NotifyToUpdateSubject -> notifyToUpdateSubject()
            SessionContract.Action.DeleteSession -> deleteSession()
            is SessionContract.Action.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = action.session)
                }
            }
            is SessionContract.Action.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = action.subject.name,
                        subjectId = action.subject.subjectId
                    )
                }
            }

            is SessionContract.Action.SaveSession -> insertSession(action.duration)
            is SessionContract.Action.UpdateSubjectIdAndRelatedSubject -> {
                _state.update {
                    it.copy(
                        relatedToSubject = action.relatedToSubject,
                        subjectId = action.subjectId
                    )
                }
            }
            else -> Unit
        }
    }

    private fun notifyToUpdateSubject() {
//        viewModelScope.launch {
//            if (state.value.subjectId == null || state.value.relatedToSubject == null) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Please select subject related to the session."
//                    )
//                )
//            }
//        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let {
                    sessionRepository.deleteSession(it)
//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "Session deleted successfully")
//                    )
                }
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't delete session. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < Constants.MINIMUM_TIMER) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Single session can not be less than 36 seconds"
//                    )
//                )
                println("Single session can not be less than 60 seconds")
                return@launch
            }
            try {
                sessionRepository.insertSession(
                    session = Session(
                        sessionSubjectId = state.value.subjectId ?: -1,
                        relatedToSubject = state.value.relatedToSubject ?: "",
                        date = Instant.now().toEpochMilli(),
                        duration = duration
                    )
                )
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(message = "Session saved successfully")
//                )
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't save session. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    fun updateTimer(
        hours: String,
        minutes: String,
        seconds: String,
        currentTimerState: TimerState
    ) {
        _state.update {
            it.copy(
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                currentTimerState = currentTimerState
            )
        }
    }

}