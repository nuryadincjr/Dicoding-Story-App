package com.nuryadincjr.storyapp.view.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.nuryadincjr.storyapp.JsonConverter
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.util.Constant
import com.nuryadincjr.storyapp.util.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
@MediumTest
class StoriesFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        Constant.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * @Ketika berhasil menampilkan data Stories di Activity
     * Memastikan RecyclerView tampil.
     * Memastikan data yang ditampilkan sesuai.
     */
    @Test
    fun getStories_Success() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
        onView(withText("Lorem Ipsum"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Lorem Ipsum 7"))
                )
            )
    }

    @Test
    fun getStories_TestError() {
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody(JsonConverter.readStringFromFile("error_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }
}