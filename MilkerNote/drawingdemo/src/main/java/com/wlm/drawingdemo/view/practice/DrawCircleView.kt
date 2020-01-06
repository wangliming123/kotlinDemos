package com.wlm.drawingdemo.view.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wlm.drawingdemo.ext.dp2px
import com.wlm.drawingdemo.ext.measureSpace

class DrawCircleView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL
        canvas?.run {
            paint.color = Color.BLACK
            drawCircle(210f, 100f, 100f, paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f
            drawCircle(500f, 100f, 100f, paint)
            paint.strokeWidth = 40f
            drawCircle(500f, 350f, 100f, paint)

            paint.color = Color.BLUE
            paint.style = Paint.Style.FILL
            drawCircle(210f, 350f, 100f, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSpace(widthMeasureSpec, dp2px(300))
        val height = measureSpace(heightMeasureSpec, dp2px(200))
        setMeasuredDimension(width, height)
    }


}