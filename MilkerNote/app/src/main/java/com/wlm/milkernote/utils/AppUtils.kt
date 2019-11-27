package com.wlm.milkernote.utils

import android.os.Build

object AppUtils {
    fun getMobileModel(): String {
        var model: String? = Build.MODEL
        model = model?.trim { it <= ' ' } ?: ""
        return model
    }

}