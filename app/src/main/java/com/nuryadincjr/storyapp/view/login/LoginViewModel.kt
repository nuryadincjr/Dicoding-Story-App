package com.nuryadincjr.storyapp.view.login

import androidx.lifecycle.ViewModel
import com.nuryadincjr.storyapp.data.repository.LoginRepository

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun onLogin(email: String, password: String) =
        loginRepository.login(email, password)
}