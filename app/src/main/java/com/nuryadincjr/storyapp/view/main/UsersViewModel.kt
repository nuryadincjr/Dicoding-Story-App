package com.nuryadincjr.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.UsersPreference
import kotlinx.coroutines.launch

class UsersViewModel(private val usersPreference: UsersPreference) : ViewModel() {
    fun getUser(): LiveData<Users> {
        return usersPreference.getUserSession().asLiveData()
    }

    fun login(user: Users) {
        viewModelScope.launch {
            usersPreference.loginSession(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            usersPreference.logoutSession()
        }
    }
}