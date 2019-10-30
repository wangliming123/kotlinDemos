package com.wlm.mvpdemos.mvp.contract

import com.wlm.mvpdemos.bean.LoginBean
import com.wlm.mvpdemos.mvp.base.IBaseView

interface LoginContract {
    interface View : IBaseView {
        fun showError(msg: String,errorCode:Int)
        fun showToast(any: Any)
        fun loginSuccess(loginBean: LoginBean)
    }

    interface Presenter {
        fun login(username: String, password: String)
    }
}