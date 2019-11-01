package com.wlm.baselib.utils

import android.os.Build

class AppUtils private constructor(){

    init {
        throw Error("Do not need instantiate!")
    }
    companion object {

        fun getMobileModel(): String {
            var model: String? = Build.MODEL
            model = model?.trim { it <= ' ' } ?: ""
            return model
        }

    }
}