package org.emunix.nullpointer.core.impl.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.impl.data.PreferencesProviderImpl
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun providePreferencesProvider(preferences: SharedPreferences): PreferencesProvider =
        PreferencesProviderImpl(preferences)
}