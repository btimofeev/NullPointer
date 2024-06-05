package org.emunix.nullpointer.uploader

import androidx.fragment.app.FragmentManager
import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher
import org.emunix.nullpointer.uploader.presentation.UploadFragment
import javax.inject.Inject

class UploadScreenLauncherImpl @Inject constructor() : UploadScreenLauncher {

    private var instance: UploadFragment? = null

    override fun launchUploadScreen(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, instance ?: UploadFragment.newInstance().also { instance = it })
            .commit()
    }
}