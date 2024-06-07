package org.emunix.nullpointer.core.impl.presentation

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.emunix.nullpointer.core.api.CHANNEL_UPLOAD_FILE
import org.emunix.nullpointer.core.api.presentation.UploadNotificationProvider
import org.emunix.nullpointer.uikit.R

class UploadNotificationProviderImpl(
    private val context: Context,
    private val activityToRunOnClick: Class<*>,
) : UploadNotificationProvider {

    override fun getNotification(): Notification {
        val notificationIntent = Intent(context, activityToRunOnClick)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            flags
        )

        return NotificationCompat.Builder(context, CHANNEL_UPLOAD_FILE)
            .setContentTitle(context.getText(R.string.app_name))
            .setContentText(context.getText(R.string.title_upload))
            .setSmallIcon(R.drawable.ic_upload_24dp)
            .setContentIntent(pendingIntent)
            .build()
    }
}