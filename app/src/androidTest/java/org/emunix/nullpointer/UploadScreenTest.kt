package org.emunix.nullpointer

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasCategories
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.emunix.nullpointer.main.MainActivity
import org.emunix.nullpointer.uploader.R
import org.emunix.nullpointer.utils.checkToolbarWithText
import org.emunix.nullpointer.utils.checkViewIsHidden
import org.emunix.nullpointer.utils.checkViewIsVisible
import org.emunix.nullpointer.utils.checkViewHasTextAndIsVisible
import org.emunix.nullpointer.utils.clickOnView
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UploadScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsRule()

    @Test
    fun checkDefaultStateOfUploadScreen() {
        checkToolbarWithText(
            id = R.id.toolbar,
            text = "Upload"
        )
        checkViewHasTextAndIsVisible(
            id = R.id.choose_file_button,
            text = "Choose file to upload"
        )
        checkViewIsVisible(R.id.anim)
        checkViewIsHidden(R.id.main_text)
        checkViewIsHidden(R.id.copy_to_clipboard_button)
        checkViewIsHidden(R.id.share_button)
        checkViewIsHidden(R.id.try_again_button)
        checkViewIsHidden(R.id.cancel_button)
    }

    @Test
    fun checkUploadFailed() {
        mockOpenDocumentIntent("content://file")

        clickOnView(R.id.choose_file_button)

        checkViewHasTextAndIsVisible(
            id = R.id.main_text,
            text = "Sorry, the upload failed"
        )
        checkViewIsVisible(R.id.try_again_button)
    }

    private fun mockOpenDocumentIntent(uri: String) {
        val imageUri = Uri.parse(uri)
        val resultData = Intent().apply { setData(imageUri) }
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        val expectedIntent: Matcher<Intent> = allOf(
            hasAction(Intent.ACTION_OPEN_DOCUMENT),
            hasCategories(setOf(Intent.CATEGORY_OPENABLE))
        )
        intending(expectedIntent).respondWith(result)
    }
}