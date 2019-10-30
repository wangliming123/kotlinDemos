package com.wlm.mvpdemos.mvp.presenter

import com.wlm.mvpdemos.mvp.base.BasePresenter
import com.wlm.mvpdemos.mvp.contract.RegisterContract
import com.wlm.mvpdemos.mvp.model.RegisterModel
import com.wlm.mvpdemos.net.ExceptionHandle

class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    private val registerModel by lazy {
        RegisterModel()
    }

    override fun register(username: String, password: String, rePassword: String) {
        checkAttached()
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            mRootView?.showToast("请输入用户名和密码")
            return
        }
        if (password != rePassword) {
            mRootView?.showToast("请输入相同的密码")
            return
        }
        mRootView?.showLoading()
        val disposable = registerModel.register(username, password, rePassword)
            .subscribe({ registerBean ->
                mRootView?.apply {
                    dismissLoading()
                    if (registerBean.data != null && registerBean.errorCode == 0 && registerBean.errorMsg.isEmpty()) {
                        registerSuccess(registerBean)
                    } else {
                        showError(registerBean.errorMsg, registerBean.errorCode)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showError(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
    }

}