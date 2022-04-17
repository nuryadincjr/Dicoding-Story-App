package com.nuryadincjr.storyapp.widget.container

import android.app.Application
import android.appwidget.AppWidgetManager.getInstance
import android.content.ComponentName
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.widget.StackWidgetProvider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class App : Application() {

    lateinit var container: ServiceContainer

    override fun onCreate() {
        super.onCreate()

        container = ServiceContainer(this)
        container.repository.dataStory.onEach {
            val component = ComponentName(this, StackWidgetProvider::class.java)
            val appWidgetIds = getInstance(applicationContext).getAppWidgetIds(component)

            container.appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetIds,
                R.id.stack_view
            )
        }.launchIn(scope = container.scope)
    }
}
