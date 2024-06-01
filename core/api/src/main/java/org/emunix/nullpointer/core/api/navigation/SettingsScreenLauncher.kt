package org.emunix.nullpointer.core.api.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface SettingsScreenLauncher {

    fun launchSettingsScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}