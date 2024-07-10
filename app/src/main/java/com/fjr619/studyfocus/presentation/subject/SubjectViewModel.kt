package com.fjr619.studyfocus.presentation.subject

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.domain.model.Task
import com.fjr619.studyfocus.domain.repository.SessionRepository
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import com.fjr619.studyfocus.domain.repository.TaskRepository
import com.fjr619.studyfocus.presentation.navArgs
import com.fjr619.studyfocus.presentation.util.toHours
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubjectViewModel(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val navArgs: SubjectScreenNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(SubjectContract.State())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectId),
        taskRepository.getCompletedTasksForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionsDurationBySubjectId(navArgs.subjectId)
    ) { state, upcomingTasks, completedTask, recentSessions, totalSessionsDuration ->
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTask,
            recentSessions = recentSessions,
            studiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectContract.State()
    )

    private val eventChannel = Channel<SubjectContract.Event>()
    val events = eventChannel.receiveAsFlow()

    //untuk reset subject ketika dia cancel update value
    lateinit var defaultValueSubject: Subject

//    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
//    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    init {
        fetchSubject()
    }

    fun onAction(action: SubjectContract.Action) {
        when (action) {
            is SubjectContract.Action.ResetSubject -> {
                _state.update {
                    it.copy(
                        subjectName = defaultValueSubject.name,
                        goalStudyHours = defaultValueSubject.goalHours.toString(),
                        subjectCardColors = defaultValueSubject.colors,
                    )
                }
            }
            is SubjectContract.Action.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = action.color)
                }
            }

            is SubjectContract.Action.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = action.name)
                }
            }

            is SubjectContract.Action.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = action.hours)
                }
            }

            is SubjectContract.Action.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = action.session)
                }
            }
            is SubjectContract.Action.OnTaskIsCompleteChange -> {
                updateTask(action.task)
            }

            SubjectContract.Action.UpdateSubject -> updateSubject()
            SubjectContract.Action.DeleteSubject -> deleteSubject()
            SubjectContract.Action.DeleteSession -> deleteSession()

            SubjectContract.Action.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository
                .getSubjectById(navArgs.subjectId)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectName = subject.name,
                            goalStudyHours = subject.goalHours.toString(),
                            subjectCardColors = subject.colors,
                            currentSubjectId = subject.subjectId
                        )
                    }

                    defaultValueSubject = subject
                }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch {
            try {
                val currentSubjectId = state.value.currentSubjectId
                if (currentSubjectId != null) {
                    withContext(Dispatchers.IO) {
                        subjectRepository.deleteSubject(subjectId = currentSubjectId)
                    }

                    eventChannel.send(SubjectContract.Event.NavigateBack)
//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "Subject deleted successfully")
//                    )
//                    _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)
                } else {
//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "No Subject to delete")
//                    )
                }
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't delete subject. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                val subject = Subject(
                    subjectId = state.value.currentSubjectId,
                    name = state.value.subjectName,
                    goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                    colors = state.value.subjectCardColors
                )

                subjectRepository.upsertSubject(
                    subject = subject
                )

                defaultValueSubject = subject

//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(message = "Subject updated successfully.")
//                )
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't update subject. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
//                if (task.isComplete) {
//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "Saved in upcoming tasks.")
//                    )
//                } else {
//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "Saved in completed tasks.")
//                    )
//                }
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't update task. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                println("aaaa ${state.value.session}")
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