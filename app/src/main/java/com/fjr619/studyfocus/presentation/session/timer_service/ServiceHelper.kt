package com.fjr619.studyfocus.presentation.session.timer_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.fjr619.studyfocus.MainActivity
import com.fjr619.studyfocus.presentation.util.Constants.CLICK_REQUEST_CODE

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
}