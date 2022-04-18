package com.nuryadincjr.storyapp.data.repository

import android.content.Context
import com.nuryadincjr.storyapp.data.local.room.StoriesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class WidgetRepository(
    scope: CoroutineScope,
    context: Context
) {
    private val database = StoriesDatabase.getInstance(context)

    val dataStory = flow {
        database.storiesDao().getWidgetStory().collect {
            emit(it)
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}