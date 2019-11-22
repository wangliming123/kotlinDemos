package com.wlm.milkernote.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.wlm.milkernote.R

class CircleView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0)

    @ColorInt
    var color: Int = Color.parseColor("#000000")

    private val paint = Paint()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        color = typedArray.getColor(R.styleable.CircleView_circleColor, Color.parseColor("#000000"))

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = color
        paint.style = Paint.Style.FILL
        val radius = if (width > height) height / 2 - 5 else width / 2 - 5
        canvas?.drawCircle(width / 2f, height / 2f, radius.toFloat(), paint)
    }
}