package com.nuryadincjr.storyapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.nuryadincjr.storyapp.R

internal class StackRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val url =
            "https://media-exp1.licdn.com/dms/image/C5603AQEXpUCSS9vDGw/profile-displayphoto-shrink_800_800/0/1629560383905?e=1655337600&v=beta&t=Op_nd-GtDMfoVxUODPCDaazKQWihAyQu858uWtcetd4"
        try {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(512, 512)
                .get()

            mWidgetItems.add(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

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