package com.wlm.drawingdemo.view.practice

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wlm.drawingdemo.ext.dp2px
import com.wlm.drawingdemo.ext.measureSpace
import com.wlm.drawingdemo.ext.reSizeBitmap

class ColorFilterView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private val bitmap = BitmapFactory.decodeStream(context.assets.open("didi.jpg"))
        .reSizeBitmap(500)


    private val paint = Paint()
    private val filterPaint = Paint()

    private val lightingColorFilter = LightingColorFilter(0x33ff88, 0x440000)
//    private val porterDuffColorFilter = PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawBitmap(bitmap, 0f, 0f, paint)
            filterPaint.colorFilter = lightingColorFilter
            drawBitmap(bitmap, 520f, 0f, filterPaint)
        }

    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSpace(widthMeasureSpec, dp2px(320))
        val height = measureSpace(heightMeasureSpec, dp2px(800))
        setMeasuredDimension(width, height)
    }
}