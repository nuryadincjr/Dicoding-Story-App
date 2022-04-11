package com.nuryadincjr.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService
import androidx.lifecycle.ViewModelProvider
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.repository.StoriesRepository
import com.nuryadincjr.storyapp.view.main.MainViewModel

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
            StackRemoteViewsFactory(this.applicationContext, intent)
}