package com.fjr619.studyfocus.data.repository

import com.fjr619.studyfocus.data.local.database.TaskDao
import com.fjr619.studyfocus.data.mapper.toTask
import com.fjr619.studyfocus.data.mapper.toTaskEntity
import com.fjr619.studyfocus.domain.model.Task
import com.fjr619.studyfocus.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl (
    private val taskDao: TaskDao
): TaskRepository {

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun getUpcomingTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllUpcomingTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map {
            it.map { taskEntity ->
                taskEntity.toTask()
            }
        }.map { tasks -> tasks.filter { it.isComplete.not() } }
            .map { tasks -> sortTasks(tasks) }
    }

    private fun sortTasks(tasks: List<Task>): List<Task> {
        return tasks.sortedWith(compareBy<Task> { it.dueDate }.thenByDescending { it.priority })
    }
}