package org.emunix.nullpointer.settings.presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import org.emunix.nullpointer.core.api.APP_THEME_PREFERENCE_KEY
import org.emunix.nullpointer.settings.R
import org.emunix.nullpointer.uikit.theme.Theme
import org.emunix.nullpointer.uikit.theme.ThemeSwitcher

internal class PreferenceView : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            APP_THEME_PREFERENCE_KEY -> {
                val theme: ListPreference? = findPreference(APP_THEME_PREFERENCE_KEY)
                if (theme != null) {
                    ThemeSwitcher().set(Theme.convertFromString(theme.value))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}