package com.wlm.drawingdemo.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View

fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}
fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}
fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.measureSpace(measureSpec: Int, default: Int): Int {
    val specMode = View.MeasureSpec.getMode(measureSpec)
    var specSize = View.MeasureSpec.getSize(measureSpec)

    if (specMode == View.MeasureSpec.EXACTLY) {//固定值或者match_parent
        return specSize
    }else if (specMode == View.MeasureSpec.AT_MOST) {//wrap_content
        specSize = default
    }
    return specSize
}

fun Bitmap.reSizeBitmap(newWidth: Int): Bitmap {

    if (width < newWidth) {
        return this
    }
    val scale = newWidth.toFloat() / width
    val matrix = Matrix()
    matrix.postScale(scale, scale)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)

}

