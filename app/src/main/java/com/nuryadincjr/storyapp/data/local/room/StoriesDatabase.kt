package com.nuryadincjr.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nuryadincjr.storyapp.data.local.entity.RemoteKeys
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.util.Constant.DB_NAME
import com.nuryadincjr.storyapp.util.Constant.DB_VERSION

@Database(
    entities = [StoryItem::class, RemoteKeys::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun storiesDao(): StoriesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoriesDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoriesDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java, DB_NAME
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
    }
}