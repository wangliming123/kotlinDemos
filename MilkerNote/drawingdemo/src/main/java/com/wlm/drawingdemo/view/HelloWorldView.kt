package com.wlm.drawingdemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class HelloWorldView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {

    }


    private val paint = Paint()

    init {
        paint.color = Color.RED
        /**
         * STROKE: 画线模式（勾边）
         * FILL: 填充模式
         * FILL_AND_STROKE: 既画线又填充
         * 默认值：FILL
         */
        paint.style = Paint.Style.STROKE
        //线条宽度10px
        paint.strokeWidth = 10f

        //抗锯齿（实际效果为修改部分边缘颜色）
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 绘制一个圆
         * cx, cy: 圆心坐标（坐标系：View左上角为（0，0））
         * radius: 半径
         */
        canvas.drawCircle(300f, 300f, 200f, paint)
        canvas.drawColor(Color.parseColor("#66660000"))
        canvas.drawARGB(100, 100, 200, 100)
        canvas.drawRGB(100, 200, 100)

        /**
         * 绘制矩形
         * left、top、right、bottom：矩形左上和右下的坐标
         */
        canvas.drawRect(100f, 100f, 400f, 400f, paint)

        /**
         * 画点
         * 点大小设置paint.strokeWidth
         * 点形状设置paint.strokeCap(ROUND: 圆形，SQUARE、BUTT：方形 )
         * paint.strokeCap这个方法不是专门用来设置点形状的，而是用来设置线条端点形状的
         */
        paint.strokeCap = Paint.Cap.SQUARE
        canvas.drawPoint(500f, 500f, paint)

        val points = floatArrayOf(
            0f, 0f, 50f, 50f, 50f, 100f, 100f,
            50f, 100f, 100f, 150f, 50f, 150f, 100f
        )
        //画多个点
        canvas.drawPoints(points, paint)
        /**
         * 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
         * offset: 2 跳过两个数，即前两个 0
         * count: 8 一共绘制 8 个数（4 个点）
         */
        canvas.drawPoints(points, 2, 8, paint);

        /**
         * 绘制椭圆
         * left、top、right、bottom：椭圆左上和右下的边界坐标
         */
        canvas.drawOval(50f, 50f, 350f, 200f, paint)

        //画线，startX, startY, stopX, stopY：起点和终点坐标
        canvas.drawLine(50f, 50f, 400f, 300f, paint)

        //批量画线
        val points2 = floatArrayOf(
            20f, 20f, 120f, 20f, 70f, 20f, 70f, 120f, 20f, 120f,
            120f, 120f, 150f, 20f, 250f, 20f, 150f, 20f, 150f, 120f,
            250f, 20f, 250f, 120f, 150f, 120f, 250f, 120f
        )
        canvas.drawLines(points2, paint);

        /**
         * 画圆角矩形
         * left top right bottom: 左上右下坐标（可以用rectF代替）
         * rx, ry : 圆角横向半径和纵向半径
         */
        canvas.drawRoundRect(50f, 50f, 400f, 400f, 50f, 50f, paint)


        paint.style = Paint.Style.FILL
        /**
         * 绘制弧形或者扇形
         * left top right bottom 弧形所在椭圆左上右下边界坐标
         * startAngle 弧形起始角度
         * sweepAngle 弧形划过角度
         * useCenter 是否连接到圆心（true为扇形）
         */
        //绘制扇形
        canvas.drawArc(200f, 100f, 800f, 500f, -110f, 100f, true, paint)
        // 绘制弧形
        canvas.drawArc(200f, 100f, 800f, 500f, 20f, 140f, false, paint)
        //绘制不封口的弧形
        paint.style = Paint.Style.STROKE
        canvas.drawArc(200f, 100f, 800f, 500f, 180f, 60f, false, paint)

        /**
         * 前面的这些方法，都是绘制某个给定的图形，而 drawPath() 可以绘制自定义图形。
         * 当你要绘制的图形比较特殊，使用前面的那些方法做不到的时候，就可以使用 drawPath() 来绘制
         * @see DrawPathView
         */


        val text = "Hello World"
        paint.textSize = 18f
        canvas.drawText(text, 100f, 25f, paint);
        paint.setTextSize(36f)
        canvas.drawText(text, 100f, 70f, paint)
        paint.setTextSize(60f)
        canvas.drawText(text, 100f, 145f, paint)
        paint.setTextSize(84f)
        canvas.drawText(text, 100f, 240f, paint)

    }

}