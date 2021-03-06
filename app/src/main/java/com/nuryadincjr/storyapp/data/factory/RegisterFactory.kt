package com.nuryadincjr.storyapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.RegisterRepository
import com.nuryadincjr.storyapp.di.Injection
import com.nuryadincjr.storyapp.view.register.RegisterViewModel

class RegisterFactory(
    private val registerRepository: RegisterRepository?,
    private val settingsPreference: SettingsPreference
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerRepository!!, settingsPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
    }

    companion object {
        @Volatile
        private var instance: RegisterFactory? = null

        fun getInstance(context: Context, settingsPreference: SettingsPreference): RegisterFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterFactory(Injection.repository(context), settingsPreference)
            }.also { instance = it }
    }
}