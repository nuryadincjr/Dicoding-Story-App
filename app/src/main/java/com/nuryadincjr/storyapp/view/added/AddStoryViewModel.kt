package com.nuryadincjr.storyapp.view.added

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddStoryViewModel(
    private val storiesRepository: StoriesRepository
) : ViewModel() {
    private val _file = MutableLiveData<File?>()

    fun setFile(file: File?) = _file.postValue(file)

    fun getFile(): LiveData<File?> = _file

    fun setToken(token: String) = storiesRepository.setToken(token)

    fun onUpload(photo: MultipartBody.Part, description: RequestBody) =
        storiesRepository.postStory(photo, description)
}