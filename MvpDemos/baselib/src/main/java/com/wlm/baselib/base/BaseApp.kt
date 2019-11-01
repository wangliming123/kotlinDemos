package com.wlm.baselib.base

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

open class BaseApp : Application() {
    companion object {
        var mContext: Context by Delegates.notNull()
            private set
        var instance: BaseApp by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        instance = this
    }
}