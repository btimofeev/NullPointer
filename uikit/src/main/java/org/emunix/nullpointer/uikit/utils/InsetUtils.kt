package org.emunix.nullpointer.uikit.utils

import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.appbar.MaterialToolbar

fun MaterialToolbar.handleSystemBarInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<MarginLayoutParams> {
            topMargin = insets.top
        }
        WindowInsetsCompat.CONSUMED
    }
}
