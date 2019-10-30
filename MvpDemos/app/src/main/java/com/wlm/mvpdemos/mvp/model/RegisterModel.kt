package com.wlm.mvpdemos.mvp.model

import com.google.gson.JsonObject
import com.wlm.mvpdemos.bean.RegisterBean
import com.wlm.mvpdemos.net.RetrofitManager
import com.wlm.mvpdemos.net.SchedulerUtils
import io.reactivex.Observable

class RegisterModel {

    fun register(username: String, password: String, rePassword: String): Observable<RegisterBean> {
        return RetrofitManager.service.register(username, password, rePassword)
            .compose(SchedulerUtils.ioToMain())
    }
}