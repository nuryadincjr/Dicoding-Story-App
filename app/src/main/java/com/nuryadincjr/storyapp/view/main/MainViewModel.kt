package com.nuryadincjr.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.repository.StoriesRepository

class MainViewModel(
    private val storiesRepository: StoriesRepository,
    private val usersPreference: UsersPreference
) : ViewModel() {
    fun setToken(token: String) = storiesRepository.setToken(token)

    fun getStories() = storiesRepository.getStories()

    fun getUser(): LiveData<Users> {
        return usersPreference.getUserSession().asLiveData()
    }
}