package com.nuryadincjr.storyapp.widget

import android.app.PendingIntent.*
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.nuryadincjr.storyapp.BuildConfig.VERSION_NAME
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.remote.response.Stories
import com.nuryadincjr.storyapp.view.main.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class ListStoryWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TOAST_ACTION = "$VERSION_NAME.TOAST_ACTION"
        const val EXTRA_ITEM = "$VERSION_NAME.EXTRA_ITEM"
        const val ITEM_LIST = "ITEM_LIST"
        private var stories: Stories? = null

        fun setList(response: Stories) {

            stories = response
        }

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                putExtra(ITEM_LIST, stories)
                data = toUri(Intent.URI_INTENT_SCHEME).toUri()
            }

            val toastIntent = Intent(context, ListStoryWidget::class.java).apply {
                action = TOAST_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            val toastPendingIntent = getBroadcast(
                context, 0, toastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    FLAG_UPDATE_CURRENT or FLAG_MUTABLE
                } else 0
            )

            val mainIntent = Intent(context, MainActivity::class.java)
            val mainPendingIntent = getActivity(
                context, 0, mainIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
                } else 0
            )

            val views = RemoteViews(context.packageName, R.layout.list_story_widget).apply {
                setRemoteAdapter(R.id.stack_view, intent)
                setEmptyView(R.id.stack_view, R.id.tv_empty)
                setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
                setOnClickPendingIntent(R.id.tv_banner, mainPendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}