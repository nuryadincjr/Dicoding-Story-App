package com.nuryadincjr.storyapp.widget

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.data.repository.WidgetRepository

class StackRemoteViewsFactory(
    private val context: Context,
    private val repository: WidgetRepository
) :
    RemoteViewsService.RemoteViewsFactory {

    private var widgetItems: List<StoryItem?>? = emptyList()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        widgetItems = repository.dataStory.value
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = widgetItems?.size ?: 0

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        if (widgetItems != null) {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(widgetItems?.get(position)?.photoUrl.toString())
                .submit(512, 512)
                .get()
            rv.setImageViewBitmap(R.id.imageView, bitmap)
            rv.setTextViewText(R.id.textView, widgetItems?.get(position)?.name.toString())
        }

        val extras = bundleOf(EXTRA_APPWIDGET_ID to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = widgetItems?.get(i).hashCode().toLong()

    override fun hasStableIds(): Boolean = false
}