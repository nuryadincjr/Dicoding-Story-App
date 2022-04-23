package com.nuryadincjr.storyapp.view.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.data.model.Users
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
class SplashScreenViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var splashScreenViewModel: SplashScreenViewModel

    private val dummyUser = DataDummy.DataDummyUser()

    @Test
    fun `When getUser Should Not Null and Return User`() {
        val user = Users(
            dummyUser.userId,
            dummyUser.name,
            dummyUser.email,
            dummyUser.password,
            dummyUser.token
        )
        val expectedUser = MutableLiveData<Users>()
        expectedUser.value = user

        `when`(splashScreenViewModel.getUser()).thenReturn(expectedUser)
        val actualUser = splashScreenViewModel.getUser().getOrAwaitValue()

        verify(splashScreenViewModel).getUser()

        assertNotNull(actualUser)
        assertEquals(user.userId, actualUser.userId)
    }

    @Test
    fun `When getTheme Should Not Null and Return True`() {
        val expectedTheme = MutableLiveData<Boolean>()
        expectedTheme.value = true

        `when`(splashScreenViewModel.getTheme()).thenReturn(expectedTheme)
        val actualTheme = splashScreenViewModel.getTheme().getOrAwaitValue()

        verify(splashScreenViewModel).getTheme()

        assertNotNull(actualTheme)
        assertTrue(actualTheme)
        assertEquals(true, actualTheme)
    }
}