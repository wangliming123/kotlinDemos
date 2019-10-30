package com.wlm.mvpdemos.mvp.presenter

import android.util.Log
import com.wlm.mvpdemos.mvp.base.BasePresenter
import com.wlm.mvpdemos.mvp.contract.LoginContract
import com.wlm.mvpdemos.mvp.model.LoginModel
import com.wlm.mvpdemos.net.ExceptionHandle

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }

    override fun login(username: String, password: String) {
        //检测是否绑定View
        checkAttached()
        if (username.isEmpty() || password.isEmpty()) {
            mRootView?.showToast("请输入用户名和密码")
            return
        }

        mRootView?.showLoading()

        val disposable = loginModel.login(username, password)
            .subscribe({ loginBean ->
                mRootView?.apply {
                    dismissLoading()
                    Log.d("111", loginBean.toString())
                    if (loginBean.data != null && loginBean.errorCode == 0 && loginBean.errorMsg.isEmpty()) {
                        loginSuccess(loginBean)
                    }else {
                        showError(loginBean.errorMsg, loginBean.errorCode)
                    }
                }

            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showError(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

}