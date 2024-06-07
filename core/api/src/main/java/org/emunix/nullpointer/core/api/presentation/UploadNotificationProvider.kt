package org.emunix.nullpointer.core.api.presentation

import android.app.Notification

interface UploadNotificationProvider {

    fun getNotification(): Notification
}