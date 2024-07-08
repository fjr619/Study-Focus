package com.fjr619.studyfocus.di

import androidx.room.Room
import com.fjr619.studyfocus.data.local.database.AppDatabase
import com.fjr619.studyfocus.data.local.database.ColorListConverter
import com.fjr619.studyfocus.data.local.database.SessionDao
import com.fjr619.studyfocus.data.local.database.SubjectDao
import com.fjr619.studyfocus.data.local.database.TaskDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "study_focus_database"
        ).addTypeConverter(ColorListConverter())
            .fallbackToDestructiveMigration()
            .build()
    }

    single<SubjectDao> { get<AppDatabase>().subjectDao() }
    single<TaskDao> { get<AppDatabase>().taskDao() }
    single<SessionDao> { get<AppDatabase>().sessionDao() }
}