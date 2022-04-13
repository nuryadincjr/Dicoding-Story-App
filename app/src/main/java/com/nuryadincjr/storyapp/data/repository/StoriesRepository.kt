package com.nuryadincjr.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRepository private constructor(
    private val apiService: ApiService
) {

    private val _token = MutableLiveData<String?>()

    fun setToken(token: String?) {
        _token.value = token
    }

    fun getStories(): LiveData<Result<List<StoryItem>>> =
        liveData {
            emit(Result.Loading)
            try {
                val keyToken = "Bearer ${_token.value}"
                val storiesResponse = apiService.getAllStories(keyToken)
                val isError = storiesResponse.error == true
                val message = storiesResponse.message.toString()
                val listStory = storiesResponse.story as List<StoryItem>

                if (isError) {
                    emit(Result.Error(message))
                } else {
                    emit(Result.Success(listStory))
                }

                Log.d(TAG, "getStories: $message")
            } catch (e: Exception) {
                Log.d(TAG, "getStories: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun postStory(
        photo: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<PostResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val keyToken = "Bearer ${_token.value}"
                val storiesResponse = apiService.addStory(keyToken, photo, description)
                val isError = storiesResponse.error == true
                val message = storiesResponse.message.toString()

                if (isError) {
                    emit(Result.Error(message))
                } else {
                    emit(Result.Success(storiesResponse))
                }

                Log.d(TAG, "postStory: $message")
            } catch (e: Exception) {
                Log.d(TAG, "postStory: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }


    companion object {
        private const val TAG = "StoriesRepository"

        @Volatile
        private var instance: StoriesRepository? = null

        fun getInstance(
            apiService: ApiService
        ): StoriesRepository = instance ?: synchronized(this) {
            instance ?: StoriesRepository(apiService)
        }.also { instance = it }
    }
}