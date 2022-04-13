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

internal class StackRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) :
    RemoteViewsService.RemoteViewsFactory {

    private val widgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        try {
            val stories = intent.getParcelableExtra<Stories>("ITEM_LIST") as Stories
            val listStory = stories.story

            if (listStory != null) {
                for (item in listStory) {
                    val bitmap: Bitmap = Glide.with(context)
                        .asBitmap()
                        .load(item.photoUrl.toString())
                        .submit(512, 512)
                        .get()
                    widgetItems.add(bitmap)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)

        rv.setImageViewBitmap(R.id.imageView, widgetItems[position])

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