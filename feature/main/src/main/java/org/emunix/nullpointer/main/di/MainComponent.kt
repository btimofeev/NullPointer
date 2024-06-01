package org.emunix.nullpointer.main.di

import dagger.Component
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.main.MainActivity

@Component(
    dependencies = [AppProvider::class]
)
interface MainComponent {

    companion object {

        fun create(appProvider: AppProvider): MainComponent {
            return DaggerMainComponent.builder().appProvider(appProvider).build()
        }
    }

    fun inject(mainActivity: MainActivity)
}