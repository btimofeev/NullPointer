package org.emunix.nullpointer

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.emunix.nullpointer.main.MainActivity
import org.emunix.nullpointer.settings.R
import org.emunix.nullpointer.utils.checkPreferenceSubtitle
import org.emunix.nullpointer.utils.checkSwitchPreferenceIsTurnOn
import org.emunix.nullpointer.utils.checkToolbarWithText
import org.emunix.nullpointer.utils.clickOnView
import org.emunix.nullpointer.utils.clickOnViewWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkDefaultStateOfSettingsScreen() {
        openSettingsScreen()
        checkToolbarWithText(
            id = R.id.toolbar,
            text = "Settings"
        )

        checkSwitchPreferenceIsTurnOn("Delete history item by swiping")
        checkPreferenceSubtitle("Application theme", "Use system default")
        checkPreferenceSubtitle("Action after upload file", "Do nothing")
        checkPreferenceSubtitle("Click on a history element", "Copy URL to clipboard")
    }

    @Test
    fun checkThemeChanges() {
        openSettingsScreen()

        clickOnViewWithText("Application theme")
        clickOnViewWithText("Dark")
        checkPreferenceSubtitle("Application theme", "Dark")
        activityRule.scenario.onActivity {
            assert(AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES)
        }

        clickOnViewWithText("Application theme")
        clickOnViewWithText("Light")
        checkPreferenceSubtitle("Application theme", "Light")
        activityRule.scenario.onActivity {
            assert(AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_NO)
        }

        clickOnViewWithText("Application theme")
        clickOnViewWithText("Use system default")
        checkPreferenceSubtitle("Application theme", "Use system default")
        activityRule.scenario.onActivity {
            assert(AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun openSettingsScreen() {
        clickOnView(org.emunix.nullpointer.main.R.id.navigation_settings)
    }
}