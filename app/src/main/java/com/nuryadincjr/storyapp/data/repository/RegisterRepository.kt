package com.nuryadincjr.storyapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.util.Constant

class RegisterRepository private constructor(
    private val context: Context,
    private val apiService: ApiService
) {
    fun register(name: String, email: String, password: String): LiveData<Result<PostResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val registerResponse = apiService.postRegister(name, email, password)
                val isError = registerResponse.error == true
                val message = registerResponse.message.toString()

                val response = if (isError) {
                    Result.Error(message)
                } else {
                    Result.Success(registerResponse)
                }

                emit(response)
                Log.d(TAG, "register: $message")
            } catch (e: Exception) {
                val exception = Constant.onException(e, context)

                emit(Result.Error(exception))
                Log.d(TAG, "register: $exception")
            }
        }

    companion object {
        private const val TAG = "RegisterRepository"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance(
            context: Context,
            apiService: ApiService
        ): RegisterRepository = instance ?: synchronized(this) {
            instance ?: RegisterRepository(context, apiService)
        }.also { instance = it }
    }
}