package com.nuryadincjr.storyapp.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerRepository: RegisterRepository,
    private val settingsPreference: SettingsPreference
) : ViewModel() {
    fun onRegister(name: String, email: String, password: String) =
        registerRepository.register(name, email, password)

    fun loginSession(user: Users) {
        viewModelScope.launch {
            settingsPreference.loginSession(user)
        }
    }
}