package org.emunix.nullpointer.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.core.api.presentation.UploadNotificationProvider
import org.emunix.nullpointer.core.impl.presentation.UploadNotificationProviderImpl
import org.emunix.nullpointer.main.MainActivity

@Module
class NotificationsModule {

    @Provides
    internal fun provideUploadNotificationProvider(context: Context): UploadNotificationProvider =
        UploadNotificationProviderImpl(context, MainActivity::class.java)
}