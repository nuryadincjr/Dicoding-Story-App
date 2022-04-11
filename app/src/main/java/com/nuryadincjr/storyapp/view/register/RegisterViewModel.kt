package com.nuryadincjr.storyapp.view.register

import androidx.lifecycle.ViewModel
import com.nuryadincjr.storyapp.data.repository.RegisterRepository

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    fun onRegister(name: String, email: String, password: String) =
        registerRepository.register(name, email, password)
}