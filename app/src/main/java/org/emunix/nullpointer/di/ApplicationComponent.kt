package org.emunix.nullpointer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.core.impl.di.DatabaseModule
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
        NavigationModule::class,
        NotificationsModule::class,
    ]
)
@Singleton
interface ApplicationComponent : AppProvider {

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}