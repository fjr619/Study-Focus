package com.fjr619.studyfocus.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.fjr619.studyfocus.R
import com.fjr619.studyfocus.data.local.database.AppDatabase
import com.fjr619.studyfocus.data.local.database.SessionDao
import com.fjr619.studyfocus.data.local.database.SubjectDao
import com.fjr619.studyfocus.data.local.database.TaskDao
import com.fjr619.studyfocus.data.repository.SessionRepositoryImpl
import com.fjr619.studyfocus.data.repository.SubjectRepositoryImpl
import com.fjr619.studyfocus.data.repository.TaskRepositoryImpl
import com.fjr619.studyfocus.domain.repository.SessionRepository
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import com.fjr619.studyfocus.domain.repository.TaskRepository
import com.fjr619.studyfocus.presentation.util.Constants.NOTIFICATION_CHANNEL_ID
import com.fjr619.studyfocus.presentation.dashboard.DashboardViewModel
import com.fjr619.studyfocus.presentation.session.timer_service.ServiceHelper
import com.fjr619.studyfocus.presentation.session.SessionViewModel
import com.fjr619.studyfocus.presentation.subject.SubjectViewModel
import com.fjr619.studyfocus.presentation.task.TaskViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "study_focus_database"
        )
            .build()
    }

    single<SubjectDao> { get<AppDatabase>().subjectDao() }
    single<TaskDao> { get<AppDatabase>().taskDao() }
    single<SessionDao> { get<AppDatabase>().sessionDao() }
}

val repositoryModule = module {
    factory<SubjectRepository> { SubjectRepositoryImpl(get(), get(), get()) }
    factory<TaskRepository> { TaskRepositoryImpl(get()) }
    factory<SessionRepository> { SessionRepositoryImpl(get()) }
}

val viewmodelModule = module {
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { SubjectViewModel(get(), get(), get(), get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
    viewModel { SessionViewModel() }
}

val notificationModule = module {
    single { NotificationCompat
        .Builder(androidContext(), NOTIFICATION_CHANNEL_ID)
        .setContentTitle("Study Session")
        .setContentText("00:00:00")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setOngoing(true)
        .setContentIntent(ServiceHelper.clickPendingIntent(androidContext())) }

    single { androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

}