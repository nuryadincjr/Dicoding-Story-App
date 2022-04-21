package com.nuryadincjr.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.nuryadincjr.storyapp.data.FakeStoriesDao
import com.nuryadincjr.storyapp.data.local.room.StoriesDao
import com.nuryadincjr.storyapp.view.DataDummy
import com.nuryadincjr.storyapp.view.MainCoroutineRule
import com.nuryadincjr.storyapp.view.getOrAwaitValue
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
class WidgetRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private lateinit var storiesDao: StoriesDao

    @Before
    fun setUp() {
        storiesDao = FakeStoriesDao()
    }

    /**
     * @Ketika getWidgetStory data sesuai.
     * Memastikan data stories tidak null.
     * Memastikan jumlah data sesuai dengan yang duharapkan.
     * Memastikan data hasil sesuai dengan yang diharapkan.
     */
    @Test
    fun `when getWidgetStory Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val expectedWidget = DataDummy.generateDummyStoriesEntity()
        storiesDao.insertStory(expectedWidget)

        val actualWidget = storiesDao.getWidgetStory().asLiveData().getOrAwaitValue()

        assertNotNull(actualWidget)
        assertEquals(expectedWidget.size, actualWidget?.size)
        assertEquals(expectedWidget[0].id, actualWidget?.get(0)?.id)
    }
}