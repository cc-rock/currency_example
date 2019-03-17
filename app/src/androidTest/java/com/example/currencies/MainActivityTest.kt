package com.example.currencies

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.util.TreeIterables
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun launchActivity_enterAmount_checkThatResultsAreDisplayed() {
        onView(withId(R.id.amount_field))
            .perform(typeText("12.5"), closeSoftKeyboard())
        onView(isRoot()).perform(waitText("JPY", 5000))
    }

    @Test
    fun select_two_rows_and_open_compare_view() {
        onView(withId(R.id.amount_field))
            .perform(typeText("12.5"), closeSoftKeyboard())
        onView(isRoot()).perform(waitText("JPY", 5000))
        onView(withText("AUD")).perform(click())
        onView(withText("CHF")).perform(click())
        onView(withText("Compare")).perform(click())
        onView(isRoot()).perform(waitText("Currency Compare", 5000))
    }

    /**
     * Perform action of waiting for a view with the given text.
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
    fun waitText(text: String, millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with text <$text> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher = withText(text)

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }
}
