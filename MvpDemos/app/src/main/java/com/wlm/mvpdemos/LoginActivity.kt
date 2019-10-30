package com.wlm.mvpdemos

import android.content.Intent
import com.wlm.mvpdemos.base.BaseActivity
import com.wlm.mvpdemos.bean.LoginBean
import com.wlm.mvpdemos.mvp.contract.LoginContract
import com.wlm.mvpdemos.mvp.presenter.LoginPresenter
import com.wlm.mvpdemos.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginContract.View {

    private val mPresenter by lazy {
        LoginPresenter()
    }

    init {
        mPresenter.attachView(this)
    }

    override fun layoutId() = R.layout.activity_login

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initListener() {
        btnLogin.setOnClickListener{
            mPresenter.login(etLoginName.text.toString(), etLoginPwd.text.toString())
        }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    /**
     * 登录成功
     */
    override fun loginSuccess(loginBean: LoginBean) {
        showToast("hello ${loginBean.data?.nickname}")
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
//        if (errorCode == ErrorStatus.NETWORK_ERROR) {
//            multipleStatusView.showNoNetwork()
//        } else {
//            multipleStatusView.showError()
//        }
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun showToast(any: Any) {
        ToastUtils.showShort(this, any)
    }

}
