package org.emunix.nullpointer.uploader.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.uploader.domain.CheckAndUploadFileUseCase
import org.emunix.nullpointer.uploader.domain.CheckAndUploadFileUseCaseImpl
import org.emunix.nullpointer.uploader.domain.UploadRepository

@Module
internal class UploadDomainModule {

    @Provides
    fun provideCheckAndUploadFileUseCase(
        context: Context,
        uploadRepository: UploadRepository,
    ): CheckAndUploadFileUseCase =
        CheckAndUploadFileUseCaseImpl(
            repository = uploadRepository,
            tempDir = context.cacheDir
        )
}