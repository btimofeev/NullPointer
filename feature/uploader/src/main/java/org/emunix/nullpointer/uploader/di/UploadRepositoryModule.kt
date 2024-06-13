package org.emunix.nullpointer.uploader.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.uploader.data.api.UploadApi
import org.emunix.nullpointer.uploader.data.repository.UploadRepositoryImpl
import org.emunix.nullpointer.uploader.domain.UploadRepository
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
internal interface UploadRepositoryModule {

    @Binds
    fun bindUploadRepository(
        impl: UploadRepositoryImpl
    ): UploadRepository


    companion object {

        @Provides
        @Singleton
        fun provideUploadApi(): UploadApi =
            Retrofit.Builder()
                .baseUrl("https://0x0.st/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(UploadApi::class.java)
    }
}