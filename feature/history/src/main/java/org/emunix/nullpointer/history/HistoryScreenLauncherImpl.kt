package org.emunix.nullpointer.history

import androidx.fragment.app.FragmentManager
import org.emunix.nullpointer.core.api.navigation.HistoryScreenLauncher
import org.emunix.nullpointer.history.presentation.HistoryFragment
import javax.inject.Inject

class HistoryScreenLauncherImpl @Inject constructor() : HistoryScreenLauncher {

    override fun launchHistoryScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, HistoryFragment.newInstance())
            .commit()
    }
}