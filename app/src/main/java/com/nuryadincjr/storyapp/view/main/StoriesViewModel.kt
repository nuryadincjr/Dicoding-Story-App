package com.nuryadincjr.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.repository.StoriesRepository

class StoriesViewModel(
    private val storiesRepository: StoriesRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {
    fun setToken(token: String) = storiesRepository.setToken(token)

    fun getUser(): LiveData<Users> = settingsPreference.getUserSession().asLiveData()

    fun getStory() = storiesRepository.getStories().cachedIn(viewModelScope)
}