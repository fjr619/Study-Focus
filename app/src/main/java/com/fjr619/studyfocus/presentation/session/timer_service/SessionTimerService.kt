package com.fjr619.studyfocus.presentation.session.timer_service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_CANCEL
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_START
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_STOP
import com.fjr619.studyfocus.presentation.util.Constants.NOTIFICATION_CHANNEL_ID
import com.fjr619.studyfocus.presentation.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.fjr619.studyfocus.presentation.util.Constants.NOTIFICATION_ID
import com.fjr619.studyfocus.presentation.util.pad
import org.koin.android.ext.android.inject
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@SuppressLint("RestrictedApi")
class SessionTimerService : Service() {

    val notificationManager: NotificationManager by inject()
    val notificationBuilder: NotificationCompat.Builder by inject()

    private val binder = SessionTimerBinder()

    private lateinit var timer: Timer
    var duration: Duration = Duration.ZERO
        private set

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentTimerState = mutableStateOf(TimerState.IDLE)
        private set

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    setPauseButton()
                    startForegroundService()
                    startTimer { hours, minutes, seconds ->
                        updateNotification(hours, minutes, seconds)
                    }
                }

                ACTION_SERVICE_STOP -> {
                    stopTimer()
                    setResumeButton()
                    setCancelButton()
                }

                ACTION_SERVICE_CANCEL -> {
                    stopTimer()
                    cancelTimer()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentText("$hours:$minutes:$seconds")
                .build()
        )
    }

    private fun startTimer(
        onTick: (h: String, m: String, s: String) -> Unit
    ) {
        currentTimerState.value = TimerState.STARTED
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentTimerState.value = TimerState.STOPPED
    }

    private fun cancelTimer() {
        duration = Duration.ZERO
        updateTimeUnits()
        currentTimerState.value = TimerState.IDLE
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@SessionTimerService.hours.value = hours.toInt().pad()
            this@SessionTimerService.minutes.value = minutes.pad()
            this@SessionTimerService.seconds.value = seconds.pad()
        }
    }


    private fun setPauseButton() {
        notificationBuilder.mActions.clear()
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Pause",
                ServiceHelper.stopPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setResumeButton() {
        notificationBuilder.mActions.clear()
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                ServiceHelper.resumePendingIntent(this)
            )
        )
    }

    private fun setCancelButton() {
        notificationBuilder.mActions.add(
            1,
            NotificationCompat.Action(
                0,
                "Cancel",
                ServiceHelper.cancelPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    inner class SessionTimerBinder : Binder() {
        fun getService(): SessionTimerService = this@SessionTimerService
    }
}

enum class TimerState {
    IDLE,
    STARTED,
    STOPPED
}