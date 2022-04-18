package com.nuryadincjr.storyapp.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.view.settings.SettingsViewModel

class SettingFactory(
    private val settingsPreference: SettingsPreference
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
    }

    companion object {
        @Volatile
        private var instance: SettingFactory? = null

        fun getInstance(settingsPreference: SettingsPreference): SettingFactory =
            instance ?: synchronized(this) {
                instance ?: SettingFactory(settingsPreference)
            }.also { instance = it }
    }
}