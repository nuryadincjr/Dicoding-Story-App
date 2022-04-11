package com.nuryadincjr.storyapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import com.nuryadincjr.storyapp.di.Injection
import com.nuryadincjr.storyapp.view.added.AddStoryViewModel
import com.nuryadincjr.storyapp.view.main.MainViewModel

class StoriesFactory(private val storiesRepository: StoriesRepository?) :
ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storiesRepository!!) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storiesRepository!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
    }

    companion object {
        @Volatile
        private var instance: StoriesFactory? = null
        fun getInstance(context: Context): StoriesFactory =
            instance ?: synchronized(this) {
                instance ?: StoriesFactory(Injection.repository(context))
            }.also { instance = it }
    }
}