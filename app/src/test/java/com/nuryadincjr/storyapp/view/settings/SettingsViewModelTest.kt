package com.nuryadincjr.storyapp.view.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
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

    @Mock
    private lateinit var settingsViewModel: SettingsViewModel

    @Test
    fun `when saveTheme Should Not Null`() {
        val expectedToken = true
        val actualToken = settingsViewModel.saveTheme(expectedToken)

        verify(settingsViewModel).saveTheme(expectedToken)
        assertNotNull(actualToken)
    }

    @Test
    fun `When getTheme Should Not Null and Return Boolean`() {
        val expectedTheme = MutableLiveData<Boolean>()
        expectedTheme.value = true

        `when`(settingsViewModel.getTheme()).thenReturn(expectedTheme)
        val actualTheme = settingsViewModel.getTheme().getOrAwaitValue()

        verify(settingsViewModel).getTheme()

        assertNotNull(actualTheme)
        assertTrue(actualTheme)
        assertEquals(true, actualTheme)
    }

    @Test
    fun `when logout Should Not Null`() {
        val actualLogout = settingsViewModel.logout()

        verify(settingsViewModel).logout()
        assertNotNull(actualLogout)
    }

    @Test
    fun `when deleteStories Should Not Null`() {
        val actualDeleteStories = settingsViewModel.deleteStories()

        verify(settingsViewModel).deleteStories()
        assertNotNull(actualDeleteStories)
    }
}