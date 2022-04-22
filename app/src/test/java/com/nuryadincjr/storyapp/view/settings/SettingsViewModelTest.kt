package com.nuryadincjr.storyapp.view.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.MainCoroutineRule
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import com.nuryadincjr.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
class SettingsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Mock
    private lateinit var settingsPreference: SettingsPreference

    @Mock
    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() {
        settingsViewModel = SettingsViewModel(storiesRepository, settingsPreference)
    }

    @Test
    fun `When getTheme Should Not Null and Return True`() =
        mainCoroutineRule.runBlockingTest {
            val expectedTheme = MutableLiveData<Boolean>()
            expectedTheme.value = true

            `when`(settingsViewModel.getTheme()).thenReturn(expectedTheme)

            val actualTheme = settingsViewModel.getTheme().getOrAwaitValue()

            verify(settingsPreference).getTheme()

            assertNotNull(actualTheme)
            assertTrue(actualTheme)
            assertEquals(true, actualTheme)
        }
}