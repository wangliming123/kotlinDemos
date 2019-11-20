package com.wlm.baselib.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


suspend fun executeResponse(
    response: CustomResponse<Any>,
    successBlock: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend CoroutineScope.() -> Unit
) {
    coroutineScope {
        if (response.errorCode == -1) errorBlock()
        else successBlock()
    }
}