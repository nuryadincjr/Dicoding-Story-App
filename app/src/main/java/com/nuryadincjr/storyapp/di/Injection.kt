package com.nuryadincjr.storyapp.di

import android.content.Context
import com.nuryadincjr.storyapp.data.local.room.StoriesDatabase
import com.nuryadincjr.storyapp.data.remote.retrofit.ApiConfig.getApiService
import com.nuryadincjr.storyapp.data.repository.LoginRepository
import com.nuryadincjr.storyapp.data.repository.RegisterRepository
import com.nuryadincjr.storyapp.data.repository.StoriesRepository

object Injection {
    inline fun <reified T> repository(context: Context): T? {
        val apiService = getApiService()
        val database = StoriesDatabase.getInstance(context)

        return when (T::class.java) {
            RegisterRepository::class.java -> {
                RegisterRepository.getInstance(context, apiService) as T
            }
            LoginRepository::class.java -> {
                LoginRepository.getInstance(context, apiService) as T
            }
            StoriesRepository::class.java -> {
                StoriesRepository.getInstance(context, apiService, database) as T
            }
            else -> null
        }
    }
}