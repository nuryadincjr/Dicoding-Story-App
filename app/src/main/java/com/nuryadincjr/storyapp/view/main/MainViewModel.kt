package com.nuryadincjr.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val storiesRepository: StoriesRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {
    fun setToken(token: String) = storiesRepository.setToken(token)

    fun getStories() = storiesRepository.getStories()

    fun getUser(): LiveData<Users> {
        return settingsPreference.getUserSession().asLiveData()
    }

    fun saveWidgetList(list: List<StoryItem>) {
        viewModelScope.launch {
            settingsPreference.saveWidgetList(list)
        }
    }
}