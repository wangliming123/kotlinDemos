package com.wlm.mvpdemos.net

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SchedulerUtils {
    fun <T> ioToMain(): IoMainScheduler<T>{
        return IoMainScheduler()
    }
}

class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())

