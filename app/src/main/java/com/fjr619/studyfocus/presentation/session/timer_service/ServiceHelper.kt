package com.fjr619.studyfocus.presentation.session.timer_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.fjr619.studyfocus.MainActivity
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_CANCEL
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_START
import com.fjr619.studyfocus.presentation.util.Constants.ACTION_SERVICE_STOP
import com.fjr619.studyfocus.presentation.util.Constants.CANCEL_REQUEST_CODE
import com.fjr619.studyfocus.presentation.util.Constants.CLICK_REQUEST_CODE
import com.fjr619.studyfocus.presentation.util.Constants.RESUME_REQUEST_CODE
import com.fjr619.studyfocus.presentation.util.Constants.STOP_REQUEST_CODE

object ServiceHelper {

    fun clickPendingIntent(context: Context): PendingIntent {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "study_focus://dashboard/session".toUri(),
            context,
            MainActivity::class.java
        )
//        return TaskStackBuilder.create(context).run {
//            addNextIntentWithParentStack(deepLinkIntent)
//            getPendingIntent(
//                CLICK_REQUEST_CODE,
//                PendingIntent.FLAG_IMMUTABLE
//            )
//        }

        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, deepLinkIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, SessionTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, SessionTimerService::class.java).apply {
            this.action = ACTION_SERVICE_STOP
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, SessionTimerService::class.java).apply {
            this.action = ACTION_SERVICE_START
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, SessionTimerService::class.java).apply {
            this.action = ACTION_SERVICE_CANCEL
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }
}