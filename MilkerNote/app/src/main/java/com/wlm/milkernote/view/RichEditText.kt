package com.wlm.milkernote.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.widget.EditText
import androidx.annotation.ColorInt
import com.wlm.milkernote.R

class RichEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    EditText(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    private val textWatcher = object : TextWatcher {
        var start = 0
        var end = 0
        override fun afterTextChanged(s: Editable?) {
            s?.run {
                setSpan(RelativeSizeSpan(relativeSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            this.start = start
            this.end = start + count
        }
    }

    @ColorInt
    var color: Int = Color.parseColor("#000000")

    var relativeSize: Float = 1.0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RichEditText)
        relativeSize = typedArray.getFloat(R.styleable.RichEditText_relativeSize, 1.0f)
        color = typedArray.getColor(R.styleable.RichEditText_richColor, Color.BLACK)
        typedArray.recycle()

        addTextChangedListener(textWatcher)
    }




}