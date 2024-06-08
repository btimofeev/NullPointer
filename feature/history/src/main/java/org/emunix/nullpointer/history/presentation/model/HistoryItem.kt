package org.emunix.nullpointer.history.presentation.model

import androidx.annotation.DrawableRes

internal data class HistoryItem(
    val fileName: String,
    val url: String,
    val uploadDate: String,
    @DrawableRes val iconRes: Int
)