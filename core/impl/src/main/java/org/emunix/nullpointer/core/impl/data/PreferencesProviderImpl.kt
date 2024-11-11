package org.emunix.nullpointer.core.impl.data

import android.content.SharedPreferences
import androidx.core.content.edit
import org.emunix.nullpointer.core.api.APP_THEME_PREFERENCE_KEY
import org.emunix.nullpointer.core.api.LAUNCH_URI_PREFERENCE_KEY
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.domain.ShareAction
import org.emunix.nullpointer.core.api.domain.ShareAction.COPY_URL_TO_CLIPBOARD
import org.emunix.nullpointer.core.api.domain.ShareAction.NONE
import org.emunix.nullpointer.core.api.domain.ShareAction.SHARE_URL
import org.emunix.nullpointer.uikit.theme.Theme
import javax.inject.Inject

class PreferencesProviderImpl @Inject constructor(
    private val preferences: SharedPreferences
) : PreferencesProvider {

    override val appTheme: Theme
        get() = Theme.convertFromString(
            themeName = preferences.getString(APP_THEME_PREFERENCE_KEY, null) ?: "default"
        )
    override val actionAfterUpload: ShareAction
        get() = convertStringToShareAction(
            action = preferences.getString("action_after_upload", null) ?: "none"
        )

    override val actionOnHistoryItemClick: ShareAction
        get() = convertStringToShareAction(
            action = preferences.getString("action_on_history_click", null) ?: "copy"
        )

    override val swipeToDeleteHistoryItem: Boolean
        get() = preferences.getBoolean("swipe_to_delete_history_item", true)

    override var launchUri: String?
        get() = preferences.getString(LAUNCH_URI_PREFERENCE_KEY, null)
        set(value) = preferences.edit { putString(LAUNCH_URI_PREFERENCE_KEY, value) }

    private fun convertStringToShareAction(action: String): ShareAction =
        when (action) {
            "share" -> SHARE_URL
            "copy" -> COPY_URL_TO_CLIPBOARD
            else -> NONE
        }
}