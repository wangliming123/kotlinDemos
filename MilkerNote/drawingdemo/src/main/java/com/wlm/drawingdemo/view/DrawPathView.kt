package com.wlm.drawingdemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * HelloWorldView中的简单绘制方法无法实现的绘制，可以用drawPath()来实现
 */
class DrawPathView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    private val paint = Paint()
    /**
     * Path 可以描述直线、二次曲线、三次曲线、圆、椭圆、弧形、矩形、圆角矩形。
     * 把这些图形结合起来，就可以描述出很多复杂的图形
     *
     * Path 有两类方法，一类是直接描述路径的，另一类是辅助的设置或计算
     */
    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        // 使用 path 对图形进行描述(心形)
//        path.addArc(200f, 200f, 400f, 400f, -225f, 225f)
//        path.arcTo(400f, 200f, 600f, 400f, -180f, 225f, false)
//        path.lineTo(400f, 542f)
//
//
//        /**
//         * addXXX(): addOval, addRect, addRoundRect, addPath
//         */
//        path.addCircle(300f, 300f, 200f, Path.Direction.CW)


        /**
         * xxxTo() 画线
         * xxxTo() 相对位置画线
         * quadTo rQuadTo 二阶贝塞尔曲线
         * cubicTo rCubicTo 三阶贝塞尔曲线
         *
         * 不论是直线还是贝塞尔曲线，都是以当前位置作为起点，而不能指定起点。
         * 但你可以通过 moveTo(x, y) 或 rMoveTo() 来改变当前位置，从而间接地设置这些方法的起点
         */
        paint.style = Paint.Style.STROKE
        path.lineTo(100f, 100f)// 由当前位置 (0, 0) 向 (100, 100) 画一条直线
        path.rLineTo(100f, 0f)// 由当前位置 (100, 100) 向正右方 100 像素的位置画一条直线

        path.moveTo(200f, 200f)
        path.lineTo(300f, 200f)


        path.lineTo(100f, 100f)
        //forceMoveTo  true：强制移动到弧形起点（无痕迹）   false：直接连线连到弧形起点（有痕迹）
//        path.arcTo(100f, 100f, 300f, 300f, -90f, 90f, true)
        path.arcTo(100f, 100f, 300f, 300f, -90f, 90f, false)

        //addArc 相当于forceMoveTo = true 的简化版 arcTo()
        path.addArc(100f, 100f, 300f, 300f, -90f, 90f)
        path.close(); // 使用 close() 封闭子图形。等价于 path.lineTo(100, 100)
        canvas.drawPath(path, paint)// 绘制出 path 描述的图形
    }

}