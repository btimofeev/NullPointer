package org.emunix.nullpointer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.emunix.nullpointer.history.R
import org.emunix.nullpointer.main.MainActivity
import org.emunix.nullpointer.utils.checkToolbarWithText
import org.emunix.nullpointer.utils.checkViewHasTextAndIsVisible
import org.emunix.nullpointer.utils.checkViewIsHidden
import org.emunix.nullpointer.utils.checkViewIsVisible
import org.emunix.nullpointer.utils.clickOnView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkDefaultStateOfHistoryScreen() {
        clickOnView(org.emunix.nullpointer.main.R.id.navigation_history)
        checkToolbarWithText(
            id = R.id.toolbar,
            text = "History"
        )
        checkViewHasTextAndIsVisible(
            id = R.id.empty_history,
            text = "The history is empty"
        )
        checkViewIsVisible(R.id.empty_history_anim)
        checkViewIsHidden(R.id.list)
    }
}