package com.nuryadincjr.storyapp.data.local.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.MainCoroutineRule
import com.nuryadincjr.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class StoriesDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var database: StoriesDatabase
    private lateinit var dao: StoriesDao

    private val dummyStories = DataDummy.generateDummyStoriesEntity()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoriesDatabase::class.java
        ).build()
        dao = database.storiesDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertStory() = mainCoroutineRule.runBlockingTest {
        dao.insertStory(dummyStories)
        val actualStories = dao.getWidgetStory().asLiveData().getOrAwaitValue()
        assertEquals(dummyStories[0].name, actualStories?.get(0)?.name)
    }

    @Test
    fun deleteAll() = mainCoroutineRule.runBlockingTest {
        dao.insertStory(dummyStories)
        dao.deleteAll()
        val actualNews = dao.getWidgetStory().asLiveData().getOrAwaitValue()
        assertTrue(actualNews?.isEmpty() ?: false)
    }
}