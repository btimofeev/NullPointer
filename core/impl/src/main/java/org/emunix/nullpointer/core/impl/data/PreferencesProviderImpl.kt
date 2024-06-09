package org.emunix.nullpointer.core.impl.data

import android.content.SharedPreferences
import org.emunix.nullpointer.core.api.APP_THEME_PREFERENCE_KEY
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.uikit.theme.Theme
import javax.inject.Inject

class PreferencesProviderImpl @Inject constructor(
    private val preferences: SharedPreferences
) : PreferencesProvider {

    override val appTheme: Theme
        get() = Theme.convertFromString(
            themeName = preferences.getString(APP_THEME_PREFERENCE_KEY, null) ?: "default"
        )
}