package org.emunix.nullpointer.core.api.domain

import org.emunix.nullpointer.uikit.theme.Theme

interface PreferencesProvider {

    val appTheme: Theme

    val actionAfterUpload: ShareAction

    val actionOnHistoryItemClick: ShareAction

    val swipeToDeleteHistoryItem: Boolean

    var launchUri: String?
}