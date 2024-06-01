package org.emunix.nullpointer.uploader.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.uploader.domain.UploadRepository
import org.emunix.nullpointer.uploader.data.api.UploadApi
import org.emunix.nullpointer.uploader.data.repository.UploadRepositoryImpl
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