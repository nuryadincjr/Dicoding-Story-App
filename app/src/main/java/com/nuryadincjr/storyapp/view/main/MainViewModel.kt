package com.nuryadincjr.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import java.io.File

class MainViewModel(
    private val storiesRepository: StoriesRepository
) : ViewModel() {
    fun setToken(token: String) = storiesRepository.setToken(token)

    fun getStories() = storiesRepository.getStories()
}