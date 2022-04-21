package com.nuryadincjr.storyapp.view.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.remote.response.LoginResponse
import com.nuryadincjr.storyapp.data.repository.RegisterRepository
import com.nuryadincjr.storyapp.view.DataDummy
import com.nuryadincjr.storyapp.view.MainCoroutineRule
import com.nuryadincjr.storyapp.view.getOrAwaitValue
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
class RegisterViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var registerViewModel: RegisterViewModel

    @Mock
    private lateinit var registerRepository: RegisterRepository

    @Mock
    private lateinit var settingsPreference: SettingsPreference

    private val dummyRegisterToLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyUser = DataDummy.DataDummyUser()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerRepository, settingsPreference)
    }

    /**
     * @Ketika berhasil registrasi data akun.
     * Memastikan data tidak null.
     * Memastikan mengembalikan Result.Success.
     * Memastikan data berhasil ditambahkan.
     * Memastikan data id user sesuai dengan yang diinginkan.
     */
    @Test
    fun `Ketika berhasil memuat menambah data Stories panggil onUpload`() =
        mainCoroutineRule.runBlockingTest {
            val expectedRegister = MutableLiveData<Result<LoginResponse>>()
            expectedRegister.value = Result.Success(dummyRegisterToLoginResponse)

            `when`(
                registerViewModel.onRegister(
                    dummyUser.name,
                    dummyUser.email,
                    dummyUser.password
                )
            ).thenReturn(expectedRegister)

            val actualRegister = registerViewModel.onRegister(
                dummyUser.name,
                dummyUser.email,
                dummyUser.password
            ).getOrAwaitValue()

            verify(registerRepository).register(
                dummyUser.name,
                dummyUser.email,
                dummyUser.password
            )

            assertNotNull(actualRegister)
            assertTrue(actualRegister is Result.Success)
            assertEquals(
                dummyRegisterToLoginResponse.message,
                (actualRegister as Result.Success).data.message
            )
            assertEquals(
                dummyRegisterToLoginResponse.loginResult?.userId,
                actualRegister.data.loginResult?.userId
            )
        }

    /**
     * @Ketika gagal registrasi data akun.
     * Memastikan mengembalikan Result.Error.
     */
    @Test
    fun `when Network Error Should Return Error`() {
        val expectedRegister = MutableLiveData<Result<LoginResponse>>()
        expectedRegister.value = Result.Error("Error")

        `when`(
            registerViewModel.onRegister(
                dummyUser.name,
                dummyUser.email,
                dummyUser.password
            )
        ).thenReturn(expectedRegister)

        val actualRegister = registerViewModel.onRegister(
            dummyUser.name,
            dummyUser.email,
            dummyUser.password
        ).getOrAwaitValue()

        verify(registerRepository).register(
            dummyUser.name,
            dummyUser.email,
            dummyUser.password
        )

        assertNotNull(actualRegister)
        assertTrue(actualRegister is Result.Error)
    }
}