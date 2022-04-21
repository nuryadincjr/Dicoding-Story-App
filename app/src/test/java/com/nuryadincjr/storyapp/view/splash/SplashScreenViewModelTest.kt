package com.nuryadincjr.storyapp.view.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nuryadincjr.storyapp.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var splashScreenViewModel: SplashScreenViewModel

    /**
     * @Ketika berhasil membuka aplikasi.
     * Memastikan data sesi log in.
     * Memastikan data berhasil log in.
     */
}