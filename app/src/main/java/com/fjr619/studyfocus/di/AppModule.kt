package com.fjr619.studyfocus.di

import androidx.room.Room
import com.fjr619.studyfocus.MainActivity
import com.fjr619.studyfocus.data.local.database.AppDatabase
import com.fjr619.studyfocus.data.local.database.ColorListConverter
import com.fjr619.studyfocus.data.local.database.SessionDao
import com.fjr619.studyfocus.data.local.database.SubjectDao
import com.fjr619.studyfocus.data.local.database.TaskDao
import com.fjr619.studyfocus.data.repository.SessionRepositoryImpl
import com.fjr619.studyfocus.data.repository.SubjectRepositoryImpl
import com.fjr619.studyfocus.data.repository.TaskRepositoryImpl
import com.fjr619.studyfocus.domain.repository.SessionRepository
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import com.fjr619.studyfocus.domain.repository.TaskRepository
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

val repositoryModule = module {
    factory<SubjectRepository> { SubjectRepositoryImpl(get()) }
    factory<TaskRepository> { TaskRepositoryImpl(get()) }
    factory<SessionRepository> { SessionRepositoryImpl(get()) }
}