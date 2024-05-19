package org.emunix.nullpointer.uploader.impl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.core.common.di.ApplicationContext
import org.emunix.nullpointer.uploader.impl.data.repository.UploadRepositoryImpl
import org.emunix.nullpointer.uploader.api.domain.UploadRepository

@Module
class UploadRepositoryModule {

    @Provides
    fun provideUploadRepository(@ApplicationContext context: Context): UploadRepository =
        UploadRepositoryImpl(tempDir = context.cacheDir)
}