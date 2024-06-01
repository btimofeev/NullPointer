package org.emunix.nullpointer.core.api.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface UploadScreenLauncher {

    fun launchUploadScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}