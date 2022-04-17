package com.nuryadincjr.storyapp.view.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val usersPreference: UsersPreference
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            usersPreference.logoutSession()
        }
    }

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            usersPreference.saveTheme(isDarkMode)
        }
    }

    fun getTheme(): LiveData<Boolean> = usersPreference.getTheme().asLiveData()

    fun saveWidgetList(list: List<StoryItem>) {
        viewModelScope.launch {
            usersPreference.saveWidgetList(list)
        }
    }
}