package com.nuryadincjr.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nuryadincjr.storyapp.data.FakeApiService
import com.nuryadincjr.storyapp.data.FakeStoriesDao
import com.nuryadincjr.storyapp.data.local.room.StoriesDao
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.view.DataDummy
import com.nuryadincjr.storyapp.view.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var storiesDao: StoriesDao

    private val dummyDescription = DataDummy.generateDummyDescription()
    private val dummyLocation = DataDummy.generateDummyLocation()
    private val dummyFile = DataDummy.generateDummyFile()
    private val dummyUser = DataDummy.DataDummyUser()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storiesDao = FakeStoriesDao()
    }

    /**
     * @Ketika getAllStories data sesuai.
     * Memastikan data stories tidak null.
     * Memastikan jumlah data sesuai dengan yang duharapkan.
     * Memastikan data hasil sesuai dengan yang diharapkan.
     */
    @Test
    fun `when getAllStories Should Not Null`() = mainCoroutineRule.runBlockingTest {
        val expectedStories = DataDummy.generateDummyStoriesResponse()
        apiService.addStory(
            dummyUser.token,
            dummyFile,
            dummyDescription,
            dummyLocation.latitude,
            dummyLocation.longitude
        )

        val actualStories = apiService.getAllStories(dummyUser.token)

        assertNotNull(actualStories)
        assertEquals(expectedStories.story?.size, actualStories.story?.size)
        assertEquals(expectedStories.story?.get(0)?.id, actualStories.story?.get(0)?.id)
    }
}