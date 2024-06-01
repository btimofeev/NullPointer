package org.emunix.nullpointer.core.api.di

import org.emunix.nullpointer.core.api.navigation.HistoryScreenLauncher
import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher

interface NavigationProvider {

    fun getUploadScreenLauncher(): UploadScreenLauncher

    fun getHistoryScreenLauncher(): HistoryScreenLauncher
}