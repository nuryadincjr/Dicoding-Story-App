package com.nuryadincjr.storyapp.view.added

import androidx.lifecycle.ViewModel
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storiesRepository: StoriesRepository
) : ViewModel() {
    fun onUpload(token: String, photo: MultipartBody.Part, description: RequestBody) =
        storiesRepository.postStory(token, photo, description)
}