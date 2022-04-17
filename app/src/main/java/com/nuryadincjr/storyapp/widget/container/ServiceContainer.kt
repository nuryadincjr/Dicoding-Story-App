package com.nuryadincjr.storyapp.widget.container

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.getInstance
import android.content.Context
import com.nuryadincjr.storyapp.data.repository.WidgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ServiceContainer(context: Context) {

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val repository = WidgetRepository(scope, context)

    val appWidgetManager: AppWidgetManager = getInstance(context)
}
