package com.wlm.mvpdemos.mvp.contract

import com.wlm.mvpdemos.bean.RegisterBean
import com.wlm.mvpdemos.mvp.base.IBaseView

interface RegisterContract {
    interface View : IBaseView {
        fun registerSuccess(registerBean: RegisterBean)
        fun showError(msg: String,errorCode:Int)
        fun showToast(any: Any)
    }

    interface Presenter {
        fun register(username: String, password: String, rePassword: String)
    }
}