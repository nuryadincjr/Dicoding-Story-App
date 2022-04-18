package com.nuryadincjr.storyapp.data.repository

import android.content.Context
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.model.SettingsPreference.Companion.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class WidgetRepository(scope: CoroutineScope, context: Context) {

    private val preferences = SettingsPreference.getInstance(context.dataStore)

    val dataStory = flow {
        preferences.getWidgetList().collect{
            emit(it)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}