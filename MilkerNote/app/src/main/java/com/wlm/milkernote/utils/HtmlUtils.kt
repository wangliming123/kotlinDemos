package com.wlm.milkernote.utils

import android.content.Context
import android.text.*
import android.text.style.*
import androidx.core.graphics.drawable.toBitmap
import com.orhanobut.logger.Logger
import java.lang.StringBuilder

object HtmlUtils {


    fun toHtml(spanned: Spanned): String {
        val sb = StringBuilder()
        val len = spanned.length
        var next: Int
        var i = 0
        while (i < len) {
            next = spanned.nextSpanTransition(i, len, CharacterStyle::class.java)
            val styles = spanned.getSpans(i, next, CharacterStyle::class.java)
            sb.append("<myFont")
            styles.forEach {
                if (it is ImageSpan) {//图片
                    val bitmapStr = BitmapUtils.bitmapToString(it.drawable.toBitmap())
                    sb.append(" bitmapString=$bitmapStr")
                } else {//文字
                    if (it is RelativeSizeSpan) {
                        val sizeEm = it.sizeChange
                        sb.append(" relativeSize=$sizeEm")
                    }
                    if (it is ForegroundColorSpan) {
                        val color = it.foregroundColor
                        sb.append(" foregroundColor=$color")
                    }
                    if (it is BackgroundColorSpan) {
                        val color = it.backgroundColor
                        sb.append(" backgroundColor=$color")
                    }
                }

            }
            sb.append(">").append(spanned.subSequence(i, next)).append("</myFont>")
            i = next
        }
        return sb.toString()
    }

    fun fromHtml(context: Context, html: String, content: String): SpannableString {
        val myFont = html.split("</myFont>")
        val spannableString = SpannableString(content)
        var start = 0
        myFont.forEach {
            if (it.isNotBlank()) {
                val text = it.substring(it.lastIndexOf(">") + 1)
                if (it.contains("bitmapString=")) {//图片
                    val bitmapStr = it.substring(
                        it.indexOf("bitmapString=") + 13,
                        it.lastIndexOf(">")
                    )
                    val bitmap = BitmapUtils.stringToBitmap(bitmapStr)
                    spannableString.setSpan(
                        ImageSpan(context, bitmap, ImageSpan.ALIGN_BASELINE),
                        start, start + text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {//文字
                    val relativeSize = it.substring(
                        it.indexOf("relativeSize") + 13,
                        it.indexOf("foregroundColor") - 1
                    ).toFloat()
                    val foregroundColor = it.substring(
                        it.indexOf("foregroundColor") + 16,
                        it.lastIndexOf(">")
                    ).toInt()
                    Logger.d("$text -- $relativeSize -- $foregroundColor")
                    spannableString.run {
                        setSpan(
                            RelativeSizeSpan(relativeSize), start, start + text.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(foregroundColor), start, start + text.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }


                start += text.length
            }
        }
        return spannableString
    }

}