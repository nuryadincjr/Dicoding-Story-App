package com.nuryadincjr.storyapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.StoryRemoteMediator
import com.nuryadincjr.storyapp.data.local.room.StoriesDatabase
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.util.Constant.onException
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRepository(
    private val context: Context,
    private val apiService: ApiService,
    private val storiesDatabase: StoriesDatabase
) {

    private val _token = MutableLiveData<String?>()

    fun setToken(token: String?) {
        _token.value = token
    }

    fun getStories(location: Int? = null): LiveData<Result<List<StoryItem>>> =
        liveData {
            emit(Result.Loading)
            try {
                val keyToken = "Bearer ${_token.value}"
                val storiesResponse = apiService.getAllStories(keyToken, location = location)
                val isError = storiesResponse.error == true
                val message = storiesResponse.message.toString()
                val listStory = storiesResponse.story as List<StoryItem>

                val response = if (isError) {
                    Result.Error(message)
                } else {
                    Result.Success(listStory)
                }

                emit(response)
                Log.d(TAG, "getStories: $message")
            } catch (e: Exception) {
                val exception = onException(e, context)

                emit(Result.Error(exception))
                Log.d(TAG, "getStories: $exception")
            }
        }

    fun postStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null,
    ): LiveData<Result<PostResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val keyToken = "Bearer ${_token.value}"
                val storiesResponse = apiService.addStory(keyToken, photo, description, lat, lon)
                val isError = storiesResponse.error == true
                val message = storiesResponse.message.toString()

                val response = if (isError) {
                    Result.Error(message)
                } else {
                    Result.Success(storiesResponse)
                }

                emit(response)
                Log.d(TAG, "postStory: $message")
            } catch (e: Exception) {
                val exception = onException(e, context)

                emit(Result.Error(exception))
                Log.d(TAG, "getStories: $exception")
            }
        }

    suspend fun deleteStories() {
        storiesDatabase.apply {
            storiesDao().deleteAll()
            remoteKeysDao().deleteRemoteKeys()
        }
    }

    fun getStories(): LiveData<PagingData<StoryItem>> {
        val keyToken = "Bearer ${_token.value}"

        @OptIn(ExperimentalPagingApi::class)
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storiesDatabase, apiService, keyToken),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStory()
            }
        ).liveData
        return pager
    }


    companion object {
        private const val TAG = "StoriesRepository"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: StoriesRepository? = null

        fun getInstance(
            context: Context,
            apiService: ApiService,
            storiesDatabase: StoriesDatabase
        ): StoriesRepository = instance ?: synchronized(this) {
            instance ?: StoriesRepository(context, apiService, storiesDatabase)
        }.also { instance = it }
    }
}