package org.emunix.nullpointer.uploader.di

import dagger.Component
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.uploader.domain.UploadRepository
import javax.inject.Singleton

@Component(
    modules = [UploadRepositoryModule::class],
    dependencies = [AppProvider::class]
)
@Singleton
interface UploadComponent {

    fun getUploadRepository(): UploadRepository

    companion object {

        fun create(appProvider: AppProvider): UploadComponent {
            return DaggerUploadComponent.builder()
                .appProvider(appProvider)
                .build()
        }
    }
}