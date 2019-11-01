package com.wlm.baselib.net

import java.io.IOException
import java.lang.Exception

open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> CustomResponse<T>): CustomResponse<T> {
        return call.invoke()
    }

    suspend fun <T: Any> saveApiCall(call: suspend () -> RequestResult<T>, errorMessage: String): RequestResult<T> {
        return try {
            call()
        }catch (e: Exception) {
            RequestResult.Error(IOException(errorMessage, e))
        }
    }
}