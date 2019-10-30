package com.wlm.mvpdemos

import android.text.method.ScrollingMovementMethod
import com.wlm.mvpdemos.base.BaseActivity
import com.wlm.mvpdemos.bean.ArticleBean
import com.wlm.mvpdemos.mvp.contract.MainContract
import com.wlm.mvpdemos.mvp.presenter.MainPresenter
import com.wlm.mvpdemos.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View {

    private val mPresenter by lazy {
        MainPresenter()
    }

    init {
        mPresenter.attachView(this)
    }

    override fun layoutId() = R.layout.activity_main

    override fun initData() {
        mPresenter.getArticles(0)
    }

    override fun initView() {
        tv_hello.movementMethod = ScrollingMovementMethod.getInstance()
    }

    override fun initListener() {

    }

    override fun bindData(articleBean: ArticleBean) {
        tv_hello.setText(articleBean.toString())
    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
    }

    override fun showToast(any: Any) {
        ToastUtils.showShort(this, any)
    }

    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

}
