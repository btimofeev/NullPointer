package org.emunix.nullpointer.settings

import androidx.fragment.app.FragmentManager
import org.emunix.nullpointer.core.api.navigation.SettingsScreenLauncher
import org.emunix.nullpointer.settings.presentation.SettingsFragment
import javax.inject.Inject

class SettingsScreenLauncherImpl @Inject constructor() : SettingsScreenLauncher {

    override fun launchSettingsScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, SettingsFragment.newInstance())
            .commit()
    }
}