package org.emunix.nullpointer

import android.app.Application
import com.google.android.material.color.DynamicColors
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.di.DaggerApplicationComponent

class App : Application(), AppProviderHolder {

    override val appProvider = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
