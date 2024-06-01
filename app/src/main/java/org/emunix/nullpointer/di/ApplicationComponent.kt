package org.emunix.nullpointer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.impl.di.DatabaseModule
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
    ]
)
@Singleton
interface ApplicationComponent : AppProvider {

    override fun getContext(): Context

    override fun getDatabaseRepository(): DatabaseRepository

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}