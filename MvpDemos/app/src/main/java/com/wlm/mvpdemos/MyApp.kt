package com.wlm.mvpdemos

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlin.properties.Delegates

class MyApp : Application() {

    companion object {
        var mContext: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

        initConfig()

    }

    /**
     * 初始化配置
     */
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) //隐藏线程信息， 默认 true
            .methodCount(0) // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(7) // 设置调用堆栈的函数偏移值
            .tag("wlm_tag")  // 每个日志的全局标记
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}