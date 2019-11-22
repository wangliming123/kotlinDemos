package com.wlm.milkernote.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun format(date: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA)
        return simpleDateFormat.format(Date(date))
    }
}