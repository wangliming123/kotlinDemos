package com.wlm.baselib.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun showShort(context: Context, any: Any) {
        Toast.makeText(context, "$any", Toast.LENGTH_SHORT).show()
    }

    fun showLong(context: Context, any: Any) {
        Toast.makeText(context, "$any", Toast.LENGTH_LONG).show()
    }
}