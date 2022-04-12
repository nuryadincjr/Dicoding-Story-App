package com.nuryadincjr.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService

class RegisterRepository private constructor(
    private val apiService: ApiService
) {
    fun register(name: String, email: String, password: String): LiveData<Result<PostResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val registerResponse = apiService.postRegister(name, email, password)
                val isError = registerResponse.error == true
                val message = registerResponse.message.toString()

                if (isError) {
                    emit(Result.Error(message))
                } else {
                    emit(Result.Success(registerResponse))
                }

                Log.d(TAG, "register: $message")
            } catch (e: Exception) {
                Log.d(TAG, "register: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        private const val TAG = "RegisterRepository"

        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance(
            apiService: ApiService
        ): RegisterRepository = instance ?: synchronized(this) {
            instance ?: RegisterRepository(apiService)
        }.also { instance = it }
    }
}