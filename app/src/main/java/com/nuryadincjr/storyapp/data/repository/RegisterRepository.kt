package com.nuryadincjr.storyapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.remote.response.LoginResponse
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiService
import com.nuryadincjr.storyapp.util.Constant.onException

class RegisterRepository private constructor(
    private val context: Context,
    private val apiService: ApiService
) {
    fun register(name: String, email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val registerResponse = apiService.postRegister(name, email, password)
                var isError = registerResponse.error == true
                var message = registerResponse.message.toString()

                if (isError) {
                    Result.Error(message)
                } else {
                    val loginResponse = apiService.postLogin(email, password)
                    isError = loginResponse.error == true
                    message = loginResponse.message.toString()

                    val response = if (isError) {
                        Result.Error(message)
                    } else {
                        Result.Success(loginResponse)
                    }
                    emit(response)
                }

                Log.d(TAG, "register: $message")
            } catch (e: Exception) {
                val exception = onException(e, context)
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