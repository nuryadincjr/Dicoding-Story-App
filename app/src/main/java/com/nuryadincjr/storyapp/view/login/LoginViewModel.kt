package com.nuryadincjr.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {

    fun onLogin(email: String, password: String) =
        loginRepository.login(email, password)

    fun loginSession(user: Users) {
        viewModelScope.launch {
            settingsPreference.loginSession(user)
        }
    }
}