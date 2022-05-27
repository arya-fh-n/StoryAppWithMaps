package com.bangkit.intermediate.storyappfinal.core.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.bangkit.intermediate.storyappfinal.R
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern


class CustomEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                when (inputType) {
                    33 -> {
                        s?.let {
                            when {
                                !Pattern.matches(
                                    "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
                                    it.trim()
                                ) -> {
                                    error = resources.getString(R.string.email) + " " + resources.getString(R.string.invalid)
                                }
                                it.isBlank() -> {
                                    error = resources.getString(R.string.required)
                                }
                            }
                        }
                    }
                    129 -> {
                        s?.let {
                            when {
                                it.length < 6 -> {
                                    error = resources.getString(R.string.min_password)
                                }
                                it.isBlank() -> {
                                    error = resources.getString(R.string.required)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

}