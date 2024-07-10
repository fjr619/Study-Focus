package com.fjr619.studyfocus.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.domain.model.Task
import com.fjr619.studyfocus.domain.repository.SessionRepository
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import com.fjr619.studyfocus.domain.repository.TaskRepository
import com.fjr619.studyfocus.presentation.util.toHours
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardContract.State())
    val state: StateFlow<DashboardContract.State> = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionsDuration()
    ) { state, subjectCount, goalHours, subjects, totalSessionDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardContract.State()
    )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    val recentSessions: StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    fun onAction(action: DashboardContract.Action) {
        when (action) {
            is DashboardContract.Action.ResetSubject -> resetNewSubject()
            is DashboardContract.Action.OnSubjectNameChange -> {
                _state.update {
                    it.copy(newSubjectName = action.name)
                }
            }

            is DashboardContract.Action.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(newSubjectGoalStudyHours = action.hours)
                }
            }

            is DashboardContract.Action.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(newSubjectCardColors = action.colors)
                }
            }

            is DashboardContract.Action.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = action.session)
                }
            }

            DashboardContract.Action.SaveSubject -> saveSubject()
            DashboardContract.Action.DeleteSession -> deleteSession()
            is DashboardContract.Action.OnTaskIsCompleteChange -> {
                updateTask(action.task)
            }
        }
    }

//    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
//    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    private fun saveSubject() {
        viewModelScope.launch {
            try {

                println("name ${state.value.newSubjectName}")
                println("goal ${state.value.newSubjectGoalStudyHours}")
                println("colors ${state.value.newSubjectCardColors}")

                subjectRepository.upsertSubject(
                    subject = Subject(
                        name = state.value.newSubjectName,
                        goalHours = state.value.newSubjectGoalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.newSubjectCardColors,
                        subjectId = null
                    )
                )

                resetNewSubject()

//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(message = "Subject saved successfully")
//                )
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't save subject. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    private fun resetNewSubject() {
        _state.update {
            it.copy(
                newSubjectGoalStudyHours = "",
                newSubjectName = "",
                newSubjectCardColors = Subject.subjectCardColors.random()
            )
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(message = "Saved in completed tasks.")
//                )
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        "Couldn't update task. ${e.message}",
//                        SnackbarDuration.Long
//                    )
//                )
            }
        }
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
}