package org.emunix.nullpointer.uploader.di

import dagger.Component
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.uploader.domain.CheckAndUploadFileUseCase
import javax.inject.Singleton

@Component(
    modules = [
        UploadDomainModule::class,
        UploadRepositoryModule::class,
    ],
    dependencies = [AppProvider::class]
)
@Singleton
interface UploadComponent {

    fun getCheckAndUploadFileUseCase(): CheckAndUploadFileUseCase

    companion object {

        fun create(appProvider: AppProvider): UploadComponent {
            return DaggerUploadComponent.builder()
                .appProvider(appProvider)
                .build()
        }
    }
}