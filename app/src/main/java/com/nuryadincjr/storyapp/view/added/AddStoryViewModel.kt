package com.nuryadincjr.storyapp.view.added

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddStoryViewModel(
    private val storiesRepository: StoriesRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {
    private val _file = MutableLiveData<File?>()
    private val _latLng = MutableLiveData<LatLng?>()

    fun setFile(file: File?) = _file.postValue(file)

    fun getFile(): LiveData<File?> = _file

    fun setLocation(latLng: LatLng?) = _latLng.postValue(latLng)

    fun getLocation(): LiveData<LatLng?> = _latLng

    fun setToken(token: String) = storiesRepository.setToken(token)

    fun onUpload(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null,
    ) = storiesRepository.postStory(photo, description, lat, lon)

    fun getUser(): LiveData<Users> = settingsPreference.getUserSession().asLiveData()
}