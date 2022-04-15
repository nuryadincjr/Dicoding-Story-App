package com.nuryadincjr.storyapp.util

import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.*
import android.content.Context
import android.util.Property
import android.view.View
import com.nuryadincjr.storyapp.R

object Constant {
    const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    const val FILENAME_FORMAT = "dd-MMM-yyyy"
    const val DATA_STORY = "data_user"
    const val MIN_PASS_LENGTH = 6
    const val SPAN_COUNT = 2
    const val PREF_SESSION = "SESSION"

    fun alphaAnim(view: View) = ofFloat(view, View.ALPHA, 1f).setDuration(500)
    fun transAnim(view: View, property: Property<View, Float>): ObjectAnimator =
        ofFloat(view, property, 30f, -30f).apply {
            duration = 6000
            repeatCount = INFINITE
            repeatMode = REVERSE
        }

    fun onException(e: Exception, context: Context): String {
        val errorCode = e.message?.filter { it.isDigit() }
        val status = if (errorCode.isNullOrEmpty()) {
            e.message.toString()
        } else errorCode

        return throwableStatus(status, context)
    }

    fun throwableStatus(status: String, context: Context) = when (status) {
        "200" -> context.getString(R.string.error_200)
        "401" -> context.getString(R.string.error_401)
        "403" -> context.getString(R.string.error_403)
        "404" -> context.getString(R.string.error_404)
        "500" -> context.getString(R.string.error_500)
        else -> status
    }
}