package com.nuryadincjr.storyapp.view.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.StoriesRepository

class MapsViewModel(
    private val storiesRepository: StoriesRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {
    fun setToken(token: String) = storiesRepository.setToken(token)

    fun getStories() = storiesRepository.getStories(1)

    fun getUser(): LiveData<Users> = settingsPreference.getUserSession().asLiveData()

    fun getTheme(): LiveData<Boolean> = settingsPreference.getTheme().asLiveData()
}