package com.nuryadincjr.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.remote.response.LoginResult

class LoginRepository private constructor(
    private val apiService: ApiService
) {
    fun login(email: String, password: String): LiveData<Result<LoginResult>> =
        liveData {
            emit(Result.Loading)
            try {
                val loginResponse = apiService.postLogin(email, password)
                val isError = loginResponse.error == true
                val message = loginResponse.message.toString()
                val loginResult = loginResponse.loginResult as LoginResult

                if (isError) {
                    emit(Result.Error(message))
                } else {
                    emit(Result.Success(loginResult))
                }

                Log.d(TAG, "login: $message ")
            } catch (e: Exception) {
                Log.d(TAG, "login: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        private const val TAG = "LoginRepository"

        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(
            apiService: ApiService
        ): LoginRepository = instance ?: synchronized(this) {
            instance ?: LoginRepository(apiService)
        }.also { instance = it }
    }
}