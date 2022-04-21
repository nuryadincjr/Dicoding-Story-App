package com.nuryadincjr.storyapp.view

import com.google.android.gms.maps.model.LatLng
import com.nuryadincjr.storyapp.data.remote.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object DataDummy {

    fun generateDummyStoriesEntity(): List<StoryItem> {
        val list = ArrayList<StoryItem>()

        for (i in 0..10) {
            val storyItem = StoryItem(
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                null,
                "Name + $i",
                "Description $i",
                -16.002,
                i.toString(),
                -10.212
            )
            list.add(storyItem)
        }
        return list
    }

    fun generateDummyStoriesResponse(): GetStoriesResponse {
        val list = ArrayList<StoryItem>()

        for (i in 0..10) {
            val storyItem = StoryItem(
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                null,
                "Name + $i",
                "Description $i",
                -16.002,
                i.toString(),
                -10.212
            )
            list.add(storyItem)
        }
        return GetStoriesResponse(list, false, "Stories fetched successfully")
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            "Nuryadin Abutani",
            "user-yj5pc_LARC_AgK67",
            "nyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_N"
        )
        return LoginResponse(loginResult, false, "success")
    }

    fun generateDummyRegisterResponse(): PostResponse {
        return PostResponse(false, "User Created")
    }

    fun generateDummyAddStoryResponse(): PostResponse {
        return PostResponse(false, "success")
    }

    fun generateDummyFile(): MultipartBody.Part {
        val fileName = "data.txt"

        val file = File(fileName)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
    }

    fun generateDummyLocation(): LatLng {
        return LatLng(-10.212, -16.002)
    }

    fun generateDummyDescription(): RequestBody {
        return "Lorem".toRequestBody("text/plain".toMediaType())
    }

    data class DataDummyUser(
        val name: String = "Nuryadin Abutani",
        val email: String = "Nuryadin.cjr@gmail.com",
        val password: String = "12345678",
        val token: String = "nyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_N"
    )
}