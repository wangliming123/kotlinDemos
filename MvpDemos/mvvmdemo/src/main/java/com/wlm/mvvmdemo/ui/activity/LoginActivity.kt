package com.wlm.mvvmdemo.ui.activity

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.wlm.baselib.base.BaseVMActivity
import com.wlm.baselib.utils.ToastUtils
import com.wlm.mvvmdemo.viewmodel.LoginViewModel
import com.wlm.mvvmdemo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseVMActivity<LoginViewModel>() {

    override fun providerVMClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun layoutId(): Int = R.layout.activity_login

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            uiState.observe(this@LoginActivity, Observer {
                if (it.showProgress) showProgressDialog()

                it.showSuccess?.let { user ->
                    dismissProgressDialog()
                    Log.d("Login", user.toString())
                    ToastUtils.showShort(this@LoginActivity, "登录成功")
                    finish()
//                    startKtxActivity<MainActivity>()
                }

                it.showError?.let { error ->
                    dismissProgressDialog()
                    ToastUtils.showShort(this@LoginActivity, error)
                }
            })
        }
    }

    override fun onError(e: Throwable) {

    }

    override fun initData() {
//        if (LoginRepository.isLogin) {
//            startKtxActivity<MainActivity>()
//        }
    }

    override fun initView() {

    }

    override fun initListener() {
        btnLogin.setOnClickListener(onClickListener)
        btnRegister.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btnLogin -> login()
            R.id.btnRegister -> register()
        }
    }

    private fun login() {

        mViewModel.login(etLoginName.text.toString(), etLoginPwd.text.toString())
    }

    private fun register() {
        mViewModel.register(etLoginName.text.toString(), etLoginPwd.text.toString())
    }

    private fun showProgressDialog() {
        loginProgress.visibility = View.VISIBLE
    }

    private fun dismissProgressDialog() {
        loginProgress.visibility = View.GONE
    }
}
