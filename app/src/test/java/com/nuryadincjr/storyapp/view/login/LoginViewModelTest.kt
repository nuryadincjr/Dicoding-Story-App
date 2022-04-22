package com.nuryadincjr.storyapp.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.remote.response.LoginResponse
import com.nuryadincjr.storyapp.data.repository.LoginRepository
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.MainCoroutineRule
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
class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var loginRepository: LoginRepository

    @Mock
    private lateinit var settingsPreference: SettingsPreference

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyUser = DataDummy.DataDummyUser()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginRepository, settingsPreference)
    }

    @Test
    fun `When onLogin Should Not Null and Return Success`() = mainCoroutineRules.runBlockingTest {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLoginResponse)

        `when`(
            loginViewModel.onLogin(
                dummyUser.email,
                dummyUser.password
            )
        ).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.onLogin(
            dummyUser.email,
            dummyUser.password
        ).getOrAwaitValue()

        verify(loginRepository).login(
            dummyUser.email,
            dummyUser.password
        )

        assertNotNull(actualLogin)
        assertTrue(actualLogin is Result.Success)
        assertEquals(dummyLoginResponse.message, (actualLogin as Result.Success).data.message)
        assertEquals(dummyLoginResponse.loginResult?.userId, actualLogin.data.loginResult?.userId)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Error("Error")

        `when`(
            loginViewModel.onLogin(
                dummyUser.email,
                dummyUser.password
            )
        ).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.onLogin(
            dummyUser.email,
            dummyUser.password
        ).getOrAwaitValue()

        verify(loginRepository).login(
            dummyUser.email,
            dummyUser.password
        )

        assertNotNull(actualLogin)
        assertTrue(actualLogin is Result.Error)
    }
}