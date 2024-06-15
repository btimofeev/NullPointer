package org.emunix.nullpointer.core.impl.data

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.domain.ShareAction
import org.emunix.nullpointer.uikit.theme.Theme
import org.junit.After
import org.junit.Before
import org.junit.Test

class PreferencesProviderImplTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferencesProvider: PreferencesProvider

    @Before
    fun setUp() {
        sharedPreferences = mockk(relaxed = true)
        preferencesProvider = PreferencesProviderImpl(sharedPreferences)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `check appTheme with LIGHT theme`() {
        every {
            sharedPreferences.getString("app_theme", null)
        } returns "light"

        val theme = preferencesProvider.appTheme

        assert(theme == Theme.LIGHT)
    }

    @Test
    fun `check appTheme with DARK theme`() {
        every {
            sharedPreferences.getString("app_theme", null)
        } returns "dark"

        val theme = preferencesProvider.appTheme

        assert(theme == Theme.DARK)
    }

    @Test
    fun `check appTheme with DEFAULT theme`() {
        every {
            sharedPreferences.getString("app_theme", null)
        } returns "default"

        val theme = preferencesProvider.appTheme

        assert(theme == Theme.DEFAULT)
    }

    @Test
    fun `check actionAfterUpload with NONE action`() {
        every {
            sharedPreferences.getString("action_after_upload", null)
        } returns "none"

        val action = preferencesProvider.actionAfterUpload

        assert(action == ShareAction.NONE)
    }

    @Test
    fun `check actionAfterUpload with SHARE_URL action`() {
        every {
            sharedPreferences.getString("action_after_upload", null)
        } returns "share"

        val action = preferencesProvider.actionAfterUpload

        assert(action == ShareAction.SHARE_URL)
    }

    @Test
    fun `check actionAfterUpload with COPY_URL_TO_CLIPBOARD action`() {
        every {
            sharedPreferences.getString("action_after_upload", null)
        } returns "copy"

        val action = preferencesProvider.actionAfterUpload

        assert(action == ShareAction.COPY_URL_TO_CLIPBOARD)
    }

    @Test
    fun `check actionOnHistoryItemClick with NONE action`() {
        every {
            sharedPreferences.getString("action_on_history_click", null)
        } returns "none"

        val action = preferencesProvider.actionOnHistoryItemClick

        assert(action == ShareAction.NONE)
    }

    @Test
    fun `check actionOnHistoryItemClick with SHARE_URL action`() {
        every {
            sharedPreferences.getString("action_on_history_click", null)
        } returns "share"

        val action = preferencesProvider.actionOnHistoryItemClick

        assert(action == ShareAction.SHARE_URL)
    }

    @Test
    fun `check actionOnHistoryItemClick with COPY_URL_TO_CLIPBOARD action`() {
        every {
            sharedPreferences.getString("action_on_history_click", null)
        } returns "copy"

        val action = preferencesProvider.actionOnHistoryItemClick

        assert(action == ShareAction.COPY_URL_TO_CLIPBOARD)
    }

    @Test
    fun `check swipeToDeleteHistoryItem with ENABLED state`() {
        every {
            sharedPreferences.getBoolean("swipe_to_delete_history_item", true)
        } returns true

        val isEnabled = preferencesProvider.swipeToDeleteHistoryItem

        assert(isEnabled)
    }
    @Test
    fun getSwipeToDeleteHistoryItem() {
    }

    @Test
    fun `check swipeToDeleteHistoryItem with DISABLED state`() {
        every {
            sharedPreferences.getBoolean("swipe_to_delete_history_item", true)
        } returns false

        val isEnabled = preferencesProvider.swipeToDeleteHistoryItem

        assert(!isEnabled)
    }
}