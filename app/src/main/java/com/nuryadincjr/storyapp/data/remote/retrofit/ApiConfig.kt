package com.nuryadincjr.storyapp.data.remote.retrofit

import com.nuryadincjr.storyapp.BuildConfig
import com.nuryadincjr.storyapp.util.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(): ApiService {
        val logger = HttpLoggingInterceptor()
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            logger.setLevel(Level.BODY)
        } else logger.setLevel(Level.NONE)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
