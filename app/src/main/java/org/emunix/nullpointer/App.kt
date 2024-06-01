package org.emunix.nullpointer

import android.app.Application
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.di.DaggerApplicationComponent

class App : Application(), AppProviderHolder {

    override val appProvider = DaggerApplicationComponent.factory().create(this)
}
