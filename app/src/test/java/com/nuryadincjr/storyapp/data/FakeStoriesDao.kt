package com.nuryadincjr.storyapp.data

import androidx.paging.PagingSource
import com.nuryadincjr.storyapp.data.local.room.StoriesDao
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeStoriesDao : StoriesDao {

    private var listStory = mutableListOf<StoryItem>()

    private val stateFlow = MutableStateFlow(listStory)

    private val flowList = stateFlow.asStateFlow()

    override suspend fun insertStory(list: List<StoryItem>) {
        listStory.addAll(list)
    }

    override fun getAllStory(): PagingSource<Int, StoryItem> {
        TODO("Not yet implemented")
    }

    override fun getWidgetStory(): Flow<List<StoryItem?>?> {
        return flowList
    }

    override suspend fun deleteAll() {
        listStory.clear()
    }
}