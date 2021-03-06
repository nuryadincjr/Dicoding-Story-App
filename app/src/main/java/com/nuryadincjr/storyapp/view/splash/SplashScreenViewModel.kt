package com.nuryadincjr.storyapp.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.SettingsPreference

class SplashScreenViewModel(
    private val settingsPreference: SettingsPreference
) : ViewModel() {

    fun getUser(): LiveData<Users> {
        return settingsPreference.getUserSession().asLiveData()
    }

    fun getTheme(): LiveData<Boolean> = settingsPreference.getTheme().asLiveData()
}