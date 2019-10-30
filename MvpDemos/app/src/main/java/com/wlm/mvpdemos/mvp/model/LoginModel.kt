package com.wlm.mvpdemos.mvp.model

import com.wlm.mvpdemos.bean.LoginBean
import com.wlm.mvpdemos.net.RetrofitManager
import com.wlm.mvpdemos.net.SchedulerUtils
import io.reactivex.Observable

/**
 * @author Milker
 * @since 2019.10.21
 * 登录model
 */
class LoginModel {

    /**
     * 登录
     */
    fun login(username: String, password: String): Observable<LoginBean> {
        return RetrofitManager.service.login(username, password)
            .compose(SchedulerUtils.ioToMain())
    }
}