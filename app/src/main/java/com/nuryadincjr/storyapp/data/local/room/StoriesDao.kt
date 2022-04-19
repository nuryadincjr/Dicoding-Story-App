package com.nuryadincjr.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<StoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryItem>

    @Query("SELECT * FROM story")
    fun getWidgetStory(): Flow<List<StoryItem?>?>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}