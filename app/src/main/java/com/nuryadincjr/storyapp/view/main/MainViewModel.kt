package com.nuryadincjr.storyapp.view.main

import androidx.lifecycle.ViewModel
import com.nuryadincjr.storyapp.data.repository.StoriesRepository

class MainViewModel(
    private val storiesRepository: StoriesRepository
) : ViewModel() {
    fun getStories(token: String) = storiesRepository.getStories(token)
}