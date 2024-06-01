package org.emunix.nullpointer.uploader

import androidx.fragment.app.FragmentManager
import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher
import org.emunix.nullpointer.uploader.presentation.UploadFragment
import javax.inject.Inject

class UploadScreenLauncherImpl @Inject constructor() : UploadScreenLauncher {

    override fun launchUploadScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .add(containerId, UploadFragment.newInstance())
            .commit()
    }
}