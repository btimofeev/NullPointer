package org.emunix.nullpointer.utils

import android.widget.Switch
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.not

fun clickOnView(id: Int) {
    onView(withId(id)).perform(click())
}

fun clickOnViewWithText(text: String) {
    onView(withText(text)).perform(click())
}

fun checkViewHasTextAndIsVisible(id: Int, text: String) {
    onView(withId(id)).check(
        matches(
            allOf(
                isDisplayed(),
                withText(text)
            )
        )
    )
}

fun checkViewIsVisible(id: Int) {
    onView(withId(id)).check(
        matches(isDisplayed())
    )
}

fun checkViewIsHidden(id: Int) {
    onView(withId(id)).check(
        matches(not(isDisplayed()))
    )
}

fun checkToolbarWithText(id: Int, text: String) {
    onView(
        allOf(
            instanceOf(TextView::class.java),
            withParent(withId(id))
        )
    )
        .check(matches(withText(text)))
}

fun checkSwitchPreferenceIsTurnOn(prefTitle: String) {
    onView(
        allOf(
            instanceOf(Switch::class.java),
            isDescendantOfA(hasDescendant(withText(prefTitle)))
        )
    ).check(matches(isChecked()))
}

fun checkPreferenceSubtitle(prefTitle: String, subtitle: String) {
    onView(
        withChild(
            withText(prefTitle)
        )
    ).check(
        matches(
            withChild(
                withText(subtitle)
            )
        )
    )
}