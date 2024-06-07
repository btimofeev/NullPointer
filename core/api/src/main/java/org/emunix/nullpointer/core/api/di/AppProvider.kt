package org.emunix.nullpointer.core.api.di

import android.content.Context
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.presentation.UploadNotificationProvider

interface AppProvider: NavigationProvider {

    fun getContext(): Context

    fun getDatabaseRepository(): DatabaseRepository

    fun getUploadNotificationProvider(): UploadNotificationProvider
}