package com.wlm.drawingdemo.view.practice

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlm.drawingdemo.ext.dp2px
import com.wlm.drawingdemo.ext.measureSpace

class ShaderView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private val paint = Paint()

    private val clampShader =
        LinearGradient(100f, 100f, 200f, 200f, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
    private val mirrorShader =
        LinearGradient(100f, 100f, 200f, 200f, Color.RED, Color.BLUE, Shader.TileMode.MIRROR)
    private val repeatShader =
        LinearGradient(100f, 100f, 200f, 200f, Color.RED, Color.BLUE, Shader.TileMode.REPEAT)

    private val radialShader =
        RadialGradient(700f, 700f, 200f, Color.RED, Color.GREEN, Shader.TileMode.MIRROR)
    private val sweepGradient = SweepGradient(200f, 1200f, Color.RED, Color.BLUE)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.shader = clampShader
        canvas?.drawRect(0f, 0f, 400f, 400f, paint)
        paint.shader = mirrorShader
        canvas?.drawRect(500f, 0f, 900f, 400f, paint)
        paint.shader = repeatShader
        canvas?.drawRect(0f, 500f, 400f, 900f, paint)

        paint.shader = radialShader
        canvas?.drawCircle(700f, 700f, 200f, paint)

        paint.shader = sweepGradient
        canvas?.drawCircle(200f, 1200f, 200f, paint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSpace(widthMeasureSpec, dp2px(300))
        val height = measureSpace(heightMeasureSpec, dp2px(600))
        setMeasuredDimension(width, height)
    }
}