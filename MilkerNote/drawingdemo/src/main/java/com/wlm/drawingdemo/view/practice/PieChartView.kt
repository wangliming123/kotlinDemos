package com.wlm.drawingdemo.view.practice

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlm.drawingdemo.ext.dp2px
import com.wlm.drawingdemo.ext.measureSpace

class PieChartView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)


    private val arcPaint = Paint()

    private val linePaint = Paint()

    private var path = Path()

    init {
        arcPaint.style = Paint.Style.FILL
        linePaint.color = Color.GRAY
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = dp2px(1).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            arcPaint.color = Color.RED
            drawArc(dp2px(50).toFloat(), dp2px(50).toFloat(),
                dp2px(250).toFloat(), dp2px(250).toFloat(),
                -180f, 120f, true, arcPaint)
            path.moveTo(dp2px(90).toFloat(), dp2px(70).toFloat())
            path.lineTo(dp2px(75).toFloat(), dp2px(50).toFloat())
            path.lineTo(dp2px(20).toFloat(), dp2px(50).toFloat())
            drawPath(path, linePaint)
            linePaint.textSize = dp2px(10).toFloat()
            drawText("red color", dp2px(20).toFloat(), dp2px(40).toFloat(), linePaint)

            arcPaint.color = Color.YELLOW
            drawArc(dp2px(50).toFloat(), dp2px(50).toFloat(),
                dp2px(250).toFloat(), dp2px(250).toFloat(),
                -60f, 60f, true, arcPaint)
            path.reset()
            path.moveTo(dp2px(230).toFloat(), dp2px(90).toFloat())
            path.lineTo(dp2px(280).toFloat(), dp2px(90).toFloat())
            drawPath(path, linePaint)
            drawText("yellow color", dp2px(230).toFloat(), dp2px(80).toFloat(), linePaint)

            arcPaint.color = Color.BLUE
            drawArc(dp2px(50).toFloat(), dp2px(50).toFloat(),
                dp2px(250).toFloat(), dp2px(250).toFloat(),
                -180f, -100f, true, arcPaint)
            path.reset()
            path.moveTo(dp2px(90).toFloat(), dp2px(230).toFloat())
            path.lineTo(dp2px(20).toFloat(), dp2px(230).toFloat())
            drawPath(path, linePaint)
            drawText("blue color", dp2px(20).toFloat(), dp2px(220).toFloat(), linePaint)

            arcPaint.color = Color.GREEN
            drawArc(dp2px(50).toFloat(), dp2px(50).toFloat(),
                dp2px(250).toFloat(), dp2px(250).toFloat(),
                0f, 80f, true, arcPaint)
            path.reset()
            path.moveTo(dp2px(230).toFloat(), dp2px(210).toFloat())
            path.lineTo(dp2px(280).toFloat(), dp2px(210).toFloat())
            drawPath(path, linePaint)
            drawText("green color", dp2px(240).toFloat(), dp2px(200).toFloat(), linePaint)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSpace(widthMeasureSpec, dp2px(300))
        val height = measureSpace(heightMeasureSpec, dp2px(300))
        setMeasuredDimension(width, height)
    }
}