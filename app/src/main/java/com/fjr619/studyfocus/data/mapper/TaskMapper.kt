package com.fjr619.studyfocus.data.mapper

import com.fjr619.studyfocus.data.local.database.entity.TaskEntity
import com.fjr619.studyfocus.domain.model.Task

fun Task.toTaskEntity(): TaskEntity = TaskEntity(
    title = title,
    description = description,
    dueDate = dueDate,
    priority = priority,
    relatedToSubject = relatedToSubject,
    isComplete = isComplete,
    taskSubjectId = taskSubjectId,
    taskId = taskId
)

fun TaskEntity.toTask(): Task = Task(
    title = title,
    description = description,
    dueDate = dueDate,
    priority = priority,
    relatedToSubject = relatedToSubject,
    isComplete = isComplete,
    taskSubjectId = taskSubjectId,
    taskId = taskId
)