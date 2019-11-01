package com.wlm.baselib.net

data class CustomResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)