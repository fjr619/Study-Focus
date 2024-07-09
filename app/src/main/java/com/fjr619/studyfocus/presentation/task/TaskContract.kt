package com.fjr619.studyfocus.presentation.task

import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.presentation.util.Priority

class TaskContract {

    data class State(
        val title: String = "",
        val description: String = "",
        val dueDate: Long? = null,
        val isTaskComplete: Boolean = false,
        val priority: Priority = Priority.LOW,
        val relatedToSubject: String? = null,
        val subjects: List<Subject> = emptyList(),
        val subjectId: Int? = null,
        val currentTaskId: Int? = null
    )

    sealed class Action {
        data class OnTitleChange(val title: String) : Action()
        data class OnDescriptionChange(val description: String) : Action()
        data class OnDateChange(val millis: Long?) : Action()
        data class OnPriorityChange(val priority: Priority) : Action()
        data class OnRelatedSubjectSelect(val subject: Subject) : Action()
        data object OnIsCompleteChange : Action()
        data object SaveTask : Action()
        data object DeleteTask : Action()
    }

    sealed interface Event {
        data object NavigateBack : Event
    }
}