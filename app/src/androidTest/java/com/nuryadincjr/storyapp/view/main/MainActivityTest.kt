package com.nuryadincjr.storyapp.view.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.util.EspressoIdlingResource
import com.nuryadincjr.storyapp.view.added.AddStoryActivity
import com.nuryadincjr.storyapp.view.detail.DetailsStoryActivity
import com.nuryadincjr.storyapp.view.location.MapsActivity
import com.nuryadincjr.storyapp.view.settings.SettingsActivity
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * @Ketika berhasil menampilkan data Stories di Activity
     * Memastikan RecyclerView tampil.
     * Memastikan data yang ditampilkan sesuai.
     */
    @Test
    fun load1Stories() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }

    /**
     * @Ketika berhasil menampilkan data Stories di Activity
     * Memastikan RecyclerView tampil.
     * Memastikan data yang ditampilkan sesuai.
     */
    @Test
    fun load2DetailStories() {
        Intents.init()
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        Intents.intended(hasComponent(DetailsStoryActivity::class.java.name))
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        pressBack()
    }

    /**
     * @Ketika berhasil menampilkan data Stories di Activity
     * Memastikan RecyclerView tampil.
     * Memastikan data yang ditampilkan sesuai.
     */
    @Test
    fun load3MapStories() {
        onView(withId(R.id.fab_location)).perform(click())
        Intents.intended(hasComponent(MapsActivity::class.java.name))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        pressBack()
    }

    /**
     * @Ketika berhasil menampilkan data Stories di Activity
     * Memastikan RecyclerView tampil.
     * Memastikan data yang ditampilkan sesuai.
     */
    @Test
    fun load4AddStory() {
        onView(withId(R.id.fab_story)).perform(click())
        Intents.intended(hasComponent(AddStoryActivity::class.java.name))
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()))
        pressBack()
    }

    /**
     * @Ketika berhasil menampilkan data Stories di Activity
     * Memastikan RecyclerView tampil.
     * Memastikan data yang ditampilkan sesuai.
     */
    @Test
    fun load5Settings() {
        onView(withId(R.id.menu_settings)).perform(click())
        Intents.intended(hasComponent(SettingsActivity::class.java.name))
        onView(withId(R.id.tvTitleFirst)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_theme)).perform(swipeDown())
        pressBack()
    }
}