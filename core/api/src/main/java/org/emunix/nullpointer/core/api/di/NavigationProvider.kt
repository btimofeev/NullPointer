package org.emunix.nullpointer.core.api.di

import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher

interface NavigationProvider {

    fun getUploadScreenLauncher(): UploadScreenLauncher
}