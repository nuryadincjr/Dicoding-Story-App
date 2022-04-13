package com.nuryadincjr.storyapp.util

import android.animation.ObjectAnimator.*
import android.util.Property
import android.view.View

object Constant {
    const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    const val FILENAME_FORMAT = "dd-MMM-yyyy"
    const val DATA_STORY = "data_user"
    const val MIN_PASS_LENGTH = 6
    const val SPAN_COUNT = 2
    const val PREF_SESSION = "SESSION"

    fun alphaAnim(view: View) = ofFloat(view, View.ALPHA, 1f).setDuration(500)
    fun transAnim(view: View, property: Property<View, Float>) =
        ofFloat(view, property, 30f, -30f).apply {
            duration = 6000
            repeatCount = INFINITE
            repeatMode = REVERSE
        }

}