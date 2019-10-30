package com.wlm.mvpdemos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wlm.mvpdemos.base.BaseActivity
import com.wlm.mvpdemos.bean.RegisterBean
import com.wlm.mvpdemos.mvp.contract.RegisterContract
import com.wlm.mvpdemos.mvp.presenter.RegisterPresenter
import com.wlm.mvpdemos.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val mPresenter by lazy {
        RegisterPresenter()
    }

    init {
        mPresenter.attachView(this)
    }

    override fun layoutId() = R.layout.activity_register

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initListener() {
        btnRegister.setOnClickListener {
            mPresenter.register(
                etRegisterName.text.toString(),
                etRegisterPwd.text.toString(),
                etRegisterPwdRepeat.text.toString()
            )
        }
    }

    override fun registerSuccess(registerBean: RegisterBean) {
        showToast("${registerBean.data?.username}注册成功")
        finish()
    }

    override fun showToast(any: Any) {
        ToastUtils.showShort(this, any)
    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

}
