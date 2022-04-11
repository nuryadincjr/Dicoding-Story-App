package com.nuryadincjr.storyapp.util

import android.content.Context
import android.text.Editable
import android.text.InputType.*
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.util.Constant.MIN_PASS_LENGTH

class MyEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val inputTypePassword = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
        val inputTypeEmail = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        if (inputType == inputTypePassword) {
            hint = context.getString(R.string.password)
        } else if (inputType == inputTypeEmail) {
            hint = context.getString(R.string.email)
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (inputType == inputTypePassword) {
                        error = if (s.isValidatePassword()) {
                            context.getString(R.string.error_password)
                        } else null
                    } else if (inputType == inputTypeEmail) {
                        error = if (!s.isValidEmail()) {
                            context.getString(R.string.error_email)
                        } else null
                    }
                }
            }

            override fun afterTextChanged(s: Editable) = Unit
        })
    }

    fun CharSequence.isValidatePassword() = this.length < MIN_PASS_LENGTH

    fun CharSequence.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
