package org.emunix.nullpointer.uploader.impl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.uploader.api.domain.UploadRepository
import org.emunix.nullpointer.uploader.impl.data.api.UploadApi
import org.emunix.nullpointer.uploader.impl.data.repository.UploadRepositoryMock
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class UploadRepositoryModule {

    @Provides
    @Singleton
    fun provideUploadApi(): UploadApi =
        Retrofit.Builder()
            .baseUrl("https://0x0.st/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(UploadApi::class.java)

    @Provides
    fun provideUploadRepository(
        context: Context,
        uploadApi: UploadApi,
    ): UploadRepository =
        UploadRepositoryImpl(
            uploadApi = uploadApi,
            tempDir = context.cacheDir,
        )
}