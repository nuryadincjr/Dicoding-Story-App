package com.nuryadincjr.storyapp.view

import com.nuryadincjr.storyapp.data.remote.response.StoryItem

object DataDummy {

    fun generateDummyStoriesResponse(): List<StoryItem> {
        val list = ArrayList<StoryItem>()

        for (i in 0..10) {
            val storyItem = StoryItem(
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                null,
                "Name + $i",
                "Description $i",
                null,
                i.toString(),
                null
            )
            list.add(storyItem)
        }
        return list
    }
}