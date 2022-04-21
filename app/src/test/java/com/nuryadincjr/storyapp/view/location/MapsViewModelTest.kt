package com.nuryadincjr.storyapp.view.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import com.nuryadincjr.storyapp.view.DataDummy
import com.nuryadincjr.storyapp.view.MainCoroutineRule
import com.nuryadincjr.storyapp.view.getOrAwaitValue
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

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var settingsPreference: SettingsPreference

    @Mock
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyStories = DataDummy.generateDummyStoriesEntity()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storiesRepository, settingsPreference)
    }

    /**
     * @Ketika berhasil memuat data Stories dengan kriteria lokasi.
     * Memastikan data tidak null.
     * Memastikan jumlah data sesuai dengan yang diharapkan.
     * Memastikan data hasil sesuai dengan yang diharapkan.
     */
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

    /**
     * @Ketika gagal memuat data Stories dengan kriteria lokasi.
     * Memastikan mengembalikan Result.Error.
     */
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
}