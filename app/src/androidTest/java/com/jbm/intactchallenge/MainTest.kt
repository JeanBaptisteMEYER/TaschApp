package com.jbm.intactchallenge

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainTest {

    @get:Rule val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addToWishlistTest() {

        //TODO change to a proper synch source
        Thread.sleep(3000)

        addProductToWishlist(0)
        addProductToWishlist(1)
        addProductToWishlist(2)
        addProductToWishlist(3)

        checkout()

        //TODO change to an automated checking process
        Thread.sleep(5000)

    }

    fun checkout() {
        onView(withId(R.id.checkout_button)).perform(scrollTo(), click())
        onView(withId(android.R.id.button1)).perform(scrollTo(), click())
    }

    fun addProductToWishlist(productPosition: Int) {

        // Click on a product in catalog recyclerview
        onView(withId(R.id.catalog_recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(productPosition, click()))

        // Add to withlist
        onView(withId(R.id.detail_add_wishlist_button)).perform(scrollTo(), click())

    }
}