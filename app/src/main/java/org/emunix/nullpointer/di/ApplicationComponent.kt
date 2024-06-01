package org.emunix.nullpointer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.emunix.nullpointer.core.api.di.ApplicationContext
import org.emunix.nullpointer.core.impl.di.DatabaseModule
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.uploader.api.domain.UploadRepository
import org.emunix.nullpointer.uploader.impl.di.UploadRepositoryModule
import javax.inject.Singleton

@Component(
    modules = [
        UploadRepositoryModule::class,
        DatabaseModule::class,
    ]
)
@Singleton
interface ApplicationComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getUploadRepository(): UploadRepository

    fun getDatabaseRepository(): DatabaseRepository

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance @ApplicationContext context: Context): ApplicationComponent
    }
}