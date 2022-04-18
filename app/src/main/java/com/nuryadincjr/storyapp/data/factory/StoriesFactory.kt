package com.nuryadincjr.storyapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import com.nuryadincjr.storyapp.di.Injection
import com.nuryadincjr.storyapp.view.added.AddStoryViewModel
import com.nuryadincjr.storyapp.view.location.MapsViewModel
import com.nuryadincjr.storyapp.view.main.MainViewModel
import com.nuryadincjr.storyapp.view.settings.SettingsViewModel
import com.nuryadincjr.storyapp.view.splash.SplashScreenViewModel

class StoriesFactory(
    private val storiesRepository: StoriesRepository?,
    private val settingsPreference: SettingsPreference?
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storiesRepository!!, settingsPreference!!) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storiesRepository!!, settingsPreference!!) as T
            }
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(settingsPreference!!) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storiesRepository!!, settingsPreference!!) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(storiesRepository!!, settingsPreference!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
        }
    }

    companion object {
        @Volatile
        private var instance: StoriesFactory? = null

        fun getInstance(context: Context, preference: SettingsPreference?): StoriesFactory =
            instance ?: synchronized(this) {
                instance ?: StoriesFactory(Injection.repository(context), preference)
            }.also { instance = it }
    }
}