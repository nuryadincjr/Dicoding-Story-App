package com.nuryadincjr.storyapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nuryadincjr.storyapp.DataDummy
import com.nuryadincjr.storyapp.MainCoroutineRule
import com.nuryadincjr.storyapp.data.FakeApiService
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var registerRepository: RegisterRepository

    @Mock
    private lateinit var context: Context

    private lateinit var apiService: ApiService

    private val dummyUser = DataDummy.DataDummyUser()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        registerRepository = RegisterRepository(context, apiService)
    }

    @Test
    fun `When postRegister Should Not Null and Return Success`() =
        mainCoroutineRules.runBlockingTest {
            val expectedRegister = DataDummy.generateDummyRegisterResponse()
            val actualRegister = apiService.postRegister(
                dummyUser.name,
                dummyUser.email,
                dummyUser.password
            )

            assertNotNull(actualRegister)
            assertEquals(expectedRegister.message, actualRegister.message)
        }
}