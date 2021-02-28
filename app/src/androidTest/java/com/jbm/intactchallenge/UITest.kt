package com.jbm.intactchallenge

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class UITest {
    @Test fun testCheckOutButton() {
        onView(withId(R.id.checkout_button)).perform(scrollTo(), click())
        //Espresso.onView(withText("Hello world!")).check(matches(isDisplayed()))
    }
}