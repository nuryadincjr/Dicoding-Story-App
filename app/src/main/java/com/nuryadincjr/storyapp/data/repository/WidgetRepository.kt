package com.nuryadincjr.storyapp.data.repository

import android.content.Context
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.model.UsersPreference.Companion.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class WidgetRepository(scope: CoroutineScope, context: Context) {

    private val preferences = UsersPreference.getInstance(context.dataStore)

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