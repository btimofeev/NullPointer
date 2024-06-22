package org.emunix.nullpointer.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.BindsInstance
import dagger.Component
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.core.impl.di.DatabaseModule
import org.emunix.nullpointer.core.impl.di.PreferencesModule
import javax.inject.Singleton

class TestApp : Application(), AppProviderHolder, Configuration.Provider {

    override val appProvider: AppProvider = DaggerTestAppComponent.factory().create(this)

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

    override fun onCreate() {
        super.onCreate()
        WorkManagerTestInitHelper.initializeTestWorkManager(this, workManagerConfiguration)
    }
}

@Singleton
@Component(
    modules = [
        DatabaseModule::class,
        NavigationModule::class,
        NotificationsModule::class,
        PreferencesModule::class,
    ]
)
interface TestAppComponent : AppProvider {

    @Component.Factory
    interface TestAppComponentFactory {

        fun create(@BindsInstance context: Context): TestAppComponent
    }
}