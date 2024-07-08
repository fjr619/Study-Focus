package com.fjr619.studyfocus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fjr619.studyfocus.data.local.database.entity.SessionEntity
import com.fjr619.studyfocus.data.local.database.entity.SubjectEntity
import com.fjr619.studyfocus.data.local.database.entity.TaskEntity

@Database(
    entities = [SubjectEntity::class, SessionEntity::class, TaskEntity::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}