package com.fjr619.studyfocus

import android.app.Application
import com.fjr619.studyfocus.di.databaseModule
import com.fjr619.studyfocus.di.notificationModule
import com.fjr619.studyfocus.di.repositoryModule
import com.fjr619.studyfocus.di.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StudyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StudyApplication)
            modules(
                databaseModule,
                repositoryModule,
                viewmodelModule,
                notificationModule
            )
        }
    }
}