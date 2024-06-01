package org.emunix.nullpointer

import android.app.Application
import android.content.Context
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.di.DaggerApplicationComponent

class App : Application(), AppProvider {

    val appComponent = DaggerApplicationComponent.factory().create(this)

    override fun getContext(): Context = appComponent.getContext()

    override fun getDatabaseRepository(): DatabaseRepository = appComponent.getDatabaseRepository()
}
