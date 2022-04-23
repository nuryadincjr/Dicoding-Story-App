package com.nuryadincjr.storyapp.view.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import com.nuryadincjr.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mapsViewModel: MapsViewModel

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var settingsPreference: SettingsPreference

    private val dummyStories = DataDummy.generateDummyStoriesEntity()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storiesRepository, settingsPreference)
    }

    @Test
    fun `when getStories Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<List<StoryItem>>>()
        expectedStories.value = Result.Success(dummyStories)

        `when`(mapsViewModel.getStories()).thenReturn(expectedStories)
        val actualStories = mapsViewModel.getStories().getOrAwaitValue()

        verify(storiesRepository).getStories(1)

        assertNotNull(actualStories)
        assertTrue(actualStories is Result.Success)
        assertEquals(dummyStories.size, (actualStories as Result.Success).data.size)
        assertEquals(dummyStories[0].id, actualStories.data[0].id)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedStories = MutableLiveData<Result<List<StoryItem>>>()
        expectedStories.value = Result.Error("Error")

        `when`(mapsViewModel.getStories()).thenReturn(expectedStories)
        val actualStories = mapsViewModel.getStories().getOrAwaitValue()

        verify(storiesRepository).getStories(1)

        assertNotNull(actualStories)
        assertTrue(actualStories is Result.Error)
    }

    @Test
    fun `When setToken Should Not Null`() {
        val expectedToken = "12345678"
        val actualToken = mapsViewModel.setToken(expectedToken)

        verify(storiesRepository).setToken(expectedToken)
        assertNotNull(actualToken)
    }
}