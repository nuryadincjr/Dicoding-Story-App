package com.nuryadincjr.storyapp.widget

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
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
        try {
            val url = widgetItems?.get(position)?.photoUrl.toString()
            val width = 187
            val height = 82
            if (url.isNotEmpty()) {
                val futureTarget: FutureTarget<Bitmap> = Glide.with(context.applicationContext)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.ic_image_load)
                    .error(R.drawable.ic_image_broken)
                    .submit(width, height)

                val bitmap = futureTarget.get()

                rv.setImageViewBitmap(R.id.imageView, bitmap)
                rv.setTextViewText(R.id.textView, widgetItems?.get(position)?.name.toString())
                val extras = bundleOf(EXTRA_APPWIDGET_ID to position)
                val fillInIntent = Intent()
                fillInIntent.putExtras(extras)

                rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = widgetItems?.get(i).hashCode().toLong()

    override fun hasStableIds(): Boolean = false
}