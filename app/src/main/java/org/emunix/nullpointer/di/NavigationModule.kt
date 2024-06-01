package org.emunix.nullpointer.di

import dagger.Binds
import dagger.Module
import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher
import org.emunix.nullpointer.uploader.UploadScreenLauncherImpl

@Module
interface NavigationModule {

    @Binds
    fun bindUploadScreenLauncher(impl: UploadScreenLauncherImpl): UploadScreenLauncher
}