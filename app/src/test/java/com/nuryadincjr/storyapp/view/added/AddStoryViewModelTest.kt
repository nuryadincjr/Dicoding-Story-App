package com.nuryadincjr.storyapp.view.added

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
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
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyViewModel: AddStoryViewModel

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var settingsPreference: SettingsPreference

    private val dummyResponse = DataDummy.generateDummyAddStoryResponse()
    private val dummyDescription = DataDummy.generateDummyDescription()
    private val dummyLocation = DataDummy.generateDummyLocation()
    private val dummyFile = DataDummy.generateDummyFile()

    @Before
    fun setUp() {
        storyViewModel = AddStoryViewModel(storiesRepository, settingsPreference)
    }

    @Test
    fun `When onUpload Should Not Null and Return Success`() {
        val expectedAddStory = MutableLiveData<Result<PostResponse>>()
        expectedAddStory.value = Result.Success(dummyResponse)

        `when`(
            storyViewModel.onUpload(
                dummyFile,
                dummyDescription,
                dummyLocation.latitude,
                dummyLocation.longitude
            )
        ).thenReturn(expectedAddStory)

        val actualAddStory = storyViewModel.onUpload(
            dummyFile,
            dummyDescription,
            dummyLocation.latitude,
            dummyLocation.longitude
        ).getOrAwaitValue()

        verify(storiesRepository).postStory(
            dummyFile,
            dummyDescription,
            dummyLocation.latitude,
            dummyLocation.longitude
        )

        assertNotNull(actualAddStory)
        assertTrue(actualAddStory is Result.Success)
        assertEquals(dummyResponse.message, (actualAddStory as Result.Success).data.message)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedAddStory = MutableLiveData<Result<PostResponse>>()
        expectedAddStory.value = Result.Error("Error")

        `when`(
            storyViewModel.onUpload(
                dummyFile,
                dummyDescription,
                dummyLocation.latitude,
                dummyLocation.longitude
            )
        ).thenReturn(expectedAddStory)

        val actualAddStory = storyViewModel.onUpload(
            dummyFile,
            dummyDescription,
            dummyLocation.latitude,
            dummyLocation.longitude
        ).getOrAwaitValue()

        verify(storiesRepository).postStory(
            dummyFile,
            dummyDescription,
            dummyLocation.latitude,
            dummyLocation.longitude
        )

        assertNotNull(actualAddStory)
        assertTrue(actualAddStory is Result.Error)
    }
}