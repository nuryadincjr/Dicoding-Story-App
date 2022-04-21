package com.nuryadincjr.storyapp.view.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nuryadincjr.storyapp.view.MainCoroutineRule
import com.nuryadincjr.storyapp.view.register.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var settingsViewModel: SettingsViewModel

    /**
     * @Ketika berhasil sign out.
     * Memastikan sesi pengguna dihapus.
     * Memastikan data stories dihapus.
     */

}