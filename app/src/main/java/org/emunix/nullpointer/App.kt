package org.emunix.nullpointer

import android.app.Application
import org.emunix.nullpointer.di.DaggerApplicationComponent

class App : Application() {

    val appComponent = DaggerApplicationComponent.factory().create(this)
}
