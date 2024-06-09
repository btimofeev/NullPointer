package org.emunix.nullpointer.uikit.theme

import androidx.appcompat.app.AppCompatDelegate
import org.emunix.nullpointer.uikit.theme.Theme.DARK
import org.emunix.nullpointer.uikit.theme.Theme.DEFAULT
import org.emunix.nullpointer.uikit.theme.Theme.LIGHT

class ThemeSwitcher {

    fun set(theme: Theme) {
        when (theme) {
            LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            DEFAULT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}