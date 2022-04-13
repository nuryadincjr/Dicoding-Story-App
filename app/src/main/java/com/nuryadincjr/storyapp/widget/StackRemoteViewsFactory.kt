package com.nuryadincjr.storyapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.remote.response.Stories
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.widget.ListStoryWidget.Companion.ITEM_LIST

internal class StackRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) :
    RemoteViewsService.RemoteViewsFactory {

    private var widgetItems: List<StoryItem>? = null

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        try {
            val stories = intent.getParcelableExtra<Stories>(ITEM_LIST) as Stories
            val listStory = stories.story
            widgetItems = listStory
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = if (widgetItems != null) widgetItems!!.size else 0

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        if (widgetItems != null) {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(widgetItems!![position].photoUrl.toString())
                .submit(512, 512)
                .get()
            rv.setImageViewBitmap(R.id.imageView, bitmap)
            rv.setTextViewText(R.id.textView, widgetItems!![position].name.toString())
        }

        val extras = bundleOf(ListStoryWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}