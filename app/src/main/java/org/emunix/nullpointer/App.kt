package org.emunix.nullpointer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Configuration
import com.google.android.material.color.DynamicColors
import org.emunix.nullpointer.core.api.CHANNEL_UPLOAD_FILE
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.di.DaggerApplicationComponent
import org.emunix.nullpointer.uikit.theme.ThemeSwitcher
import org.emunix.nullpointer.work.AppWorkerFactory

class App : Application(), AppProviderHolder, Configuration.Provider {

    override val appProvider = DaggerApplicationComponent.factory().create(this)

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(AppWorkerFactory(appProvider))
            .build()

    override fun onCreate() {
        super.onCreate()

        setupNotificationChannels()
        ThemeSwitcher().set(appProvider.getPreferencesProvider().appTheme)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    private fun setupNotificationChannels() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_LOW

        val name = getString(org.emunix.nullpointer.uikit.R.string.title_upload)
        val channel = NotificationChannel(CHANNEL_UPLOAD_FILE, name, importance)
        notificationManager.createNotificationChannel(channel)
    }
}
