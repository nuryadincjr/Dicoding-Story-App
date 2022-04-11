package com.nuryadincjr.storyapp.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.view.main.UsersViewModel

class UsersFactory(private val usersPreference: UsersPreference?) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return UsersViewModel(usersPreference!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
    }
}