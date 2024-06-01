package org.emunix.nullpointer.core.api.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface HistoryScreenLauncher {

    fun launchHistoryScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}