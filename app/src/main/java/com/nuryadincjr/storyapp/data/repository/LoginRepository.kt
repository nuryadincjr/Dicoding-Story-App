package com.nuryadincjr.storyapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.remote.response.LoginResponse
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.util.Constant

class LoginRepository(
    private val context: Context,
    private val apiService: ApiService
) {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val loginResponse = apiService.postLogin(email, password)
                val isError = loginResponse.error == true
                val message = loginResponse.message.toString()

                val response = if (isError) {
                    Result.Error(message)
                } else {
                    Result.Success(loginResponse)
                }

                emit(response)
                Log.d(TAG, "login: $message ")
            } catch (e: Exception) {
                val exception = Constant.onException(e, context)

                emit(Result.Error(exception))
                Log.d(TAG, "login: $exception")
            }
        }

    companion object {
        private const val TAG = "LoginRepository"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(
            context: Context,
            apiService: ApiService
        ): LoginRepository = instance ?: synchronized(this) {
            instance ?: LoginRepository(context, apiService)
        }.also { instance = it }
    }
}