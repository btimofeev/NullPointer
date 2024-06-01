package org.emunix.nullpointer.core.api.di

import android.content.Context
import org.emunix.nullpointer.core.api.domain.DatabaseRepository

interface AppProvider {

    fun getContext(): Context

    fun getDatabaseRepository(): DatabaseRepository
}