package com.nuryadincjr.storyapp.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.UsersPreference

class SplashScreenViewModel(
    private val usersPreference: UsersPreference
) : ViewModel() {

    fun getUser(): LiveData<Users> {
        return usersPreference.getUserSession().asLiveData()
    }

    fun getTheme(): LiveData<Boolean> = usersPreference.getTheme().asLiveData()
}