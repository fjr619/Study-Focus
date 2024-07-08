package com.fjr619.studyfocus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fjr619.studyfocus.data.local.database.entity.Session
import com.fjr619.studyfocus.data.local.database.entity.Subject
import com.fjr619.studyfocus.data.local.database.entity.Task

@Database(
    entities = [Subject::class, Session::class, Task::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}