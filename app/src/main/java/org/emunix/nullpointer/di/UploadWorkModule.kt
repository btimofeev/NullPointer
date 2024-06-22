package org.emunix.nullpointer.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.core.api.domain.UploadWorkManager
import org.emunix.nullpointer.uploader.work.UploadWorkManagerImpl

@Module
class UploadWorkModule {

    @Provides
    internal fun provideUploadNotificationProvider(context: Context): UploadWorkManager =
        UploadWorkManagerImpl(context)
}