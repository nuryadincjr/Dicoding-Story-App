package com.nuryadincjr.storyapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.repository.LoginRepository
import com.nuryadincjr.storyapp.di.Injection
import com.nuryadincjr.storyapp.view.login.LoginViewModel

class LoginFactory(
    private val loginRepository: LoginRepository?,
    private val usersPreference: UsersPreference
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository!!, usersPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
    }

    companion object {
        @Volatile
        private var instance: LoginFactory? = null

        fun getInstance(context: Context, usersPreference: UsersPreference): LoginFactory =
            instance ?: synchronized(this) {
                instance ?: LoginFactory(Injection.repository(context), usersPreference)
            }.also { instance = it }
    }
}