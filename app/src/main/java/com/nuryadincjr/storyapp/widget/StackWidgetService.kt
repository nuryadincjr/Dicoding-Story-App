package com.nuryadincjr.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.nuryadincjr.storyapp.widget.container.App


class StackWidgetService : RemoteViewsService() {

    private val container by lazy { (applicationContext as App).container }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(applicationContext, container.repository)
    }
}