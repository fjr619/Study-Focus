package com.fjr619.studyfocus.presentation.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.studyfocus.domain.model.Task
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import com.fjr619.studyfocus.domain.repository.TaskRepository
import com.fjr619.studyfocus.presentation.navArgs
import com.fjr619.studyfocus.presentation.util.Priority
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
import java.time.Instant

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: TaskScreenNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(TaskContract.State())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects()
    ) { state, subjects ->
        state.copy(subjects = subjects)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = TaskContract.State()
    )

    private val eventChannel = Channel<TaskContract.Event>()
    val events = eventChannel.receiveAsFlow()

    init {
        fetchTask()
        fetchSubject()
    }

    fun onAction(action: TaskContract.Action) {
        when (action) {
            is TaskContract.Action.OnTitleChange -> {
                _state.update {
                    it.copy(title = action.title)
                }
            }

            is TaskContract.Action.OnDescriptionChange -> {
                _state.update {
                    it.copy(description = action.description)
                }
            }

            is TaskContract.Action.OnDateChange -> {
                _state.update {
                    it.copy(dueDate = action.millis)
                }
            }

            is TaskContract.Action.OnPriorityChange -> {
                _state.update {
                    it.copy(priority = action.priority)
                }
            }

            TaskContract.Action.OnIsCompleteChange -> {
                _state.update {
                    it.copy(isTaskComplete = !_state.value.isTaskComplete)
                }
            }

            is TaskContract.Action.OnRelatedSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = action.subject.name,
                        subjectId = action.subject.subjectId
                    )
                }
            }

            TaskContract.Action.SaveTask -> saveTask()
            TaskContract.Action.DeleteTask -> deleteTask()
        }
    }

    private fun fetchTask() {
        viewModelScope.launch {
            navArgs.taskId?.let { id ->
                taskRepository.getTaskById(id)?.let { task ->
                    _state.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            dueDate = task.dueDate,
                            isTaskComplete = task.isComplete,
                            relatedToSubject = task.relatedToSubject,
                            priority = Priority.fromInt(task.priority),
                            subjectId = task.taskSubjectId,
                            currentTaskId = task.taskId
                        )
                    }
                }
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            navArgs.subjectId?.let { id ->
                subjectRepository.getSubjectById(id)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectId = subject.subjectId,
                            relatedToSubject = subject.name
                        )
                    }
                }
            }
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            try {
                val currentTaskId = state.value.currentTaskId
                if (currentTaskId != null) {
                    withContext(Dispatchers.IO) {
                        taskRepository.deleteTask(taskId = currentTaskId)
                    }

                    eventChannel.send(TaskContract.Event.NavigateBack)

//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "Task deleted successfully")
//                    )
//                    _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)
                } else {
//                    _snackbarEventFlow.emit(
//                        SnackbarEvent.ShowSnackbar(message = "No Task to delete")
//                    )
                }
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't delete task. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state = _state.value
            if (state.subjectId == null || state.relatedToSubject == null) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Please select subject related to the task"
//                    )
//                )
                return@launch
            }
            try {
                taskRepository.upsertTask(
                    task = Task(
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate ?: Instant.now().toEpochMilli(),
                        relatedToSubject = state.relatedToSubject,
                        priority = state.priority.value,
                        isComplete = state.isTaskComplete,
                        taskSubjectId = state.subjectId,
                        taskId = state.currentTaskId
                    )
                )

                eventChannel.send(TaskContract.Event.NavigateBack)

//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(message = "Task Saved Successfully")
//                )
//                _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)
            } catch (e: Exception) {
//                _snackbarEventFlow.emit(
//                    SnackbarEvent.ShowSnackbar(
//                        message = "Couldn't save task. ${e.message}",
//                        duration = SnackbarDuration.Long
//                    )
//                )
            }
        }
    }
}