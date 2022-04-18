package com.nuryadincjr.storyapp.view.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val storiesRepository: StoriesRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            settingsPreference.logoutSession()
        }
    }

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            settingsPreference.saveTheme(isDarkMode)
        }
    }

    fun getTheme(): LiveData<Boolean> = settingsPreference.getTheme().asLiveData()

    fun deleteStories() {
        viewModelScope.launch {
            storiesRepository.deleteStories()
        }
    }
}