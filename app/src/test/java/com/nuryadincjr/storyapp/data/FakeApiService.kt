package com.nuryadincjr.storyapp.data

import com.nuryadincjr.storyapp.data.remote.response.GetStoriesResponse
import com.nuryadincjr.storyapp.data.remote.response.LoginResponse
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()

    override suspend fun postRegister(name: String, email: String, password: String): PostResponse {
        return dummyRegisterResponse
    }

    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): PostResponse {
        return dummyAddStoryResponse
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): GetStoriesResponse {
        return dummyStoriesResponse
    }
}