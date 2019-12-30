package com.wlm.mvvm_wanandroid.base.ui

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.base.BaseViewModel

abstract class BaseVMFragment<VM : BaseViewModel> : BaseFragment() {
    lateinit var mViewModel: VM
    abstract val providerVMClass: Class<VM>
    protected var multipleStatusView: MultipleStatusView? = null

    override fun init() {
        initVM()
        multipleStatusView = childMultipleStatusView()
        multipleStatusView?.setOnClickListener {
            when (multipleStatusView?.viewStatus) {
                MultipleStatusView.STATUS_ERROR, MultipleStatusView.STATUS_EMPTY ->
                    retry()
            }
        }
        startObserve()
    }

    open fun childMultipleStatusView() : MultipleStatusView? = null

    open fun retry() {

    }

    open fun startObserve() {
        mViewModel.mException.observe(this, Observer { onError(it) })
    }

    private fun initVM() {
        mViewModel = ViewModelProviders.of(this).get(providerVMClass)
        lifecycle.addObserver(mViewModel)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(mViewModel)
        super.onDestroy()
    }

    open fun onError(e: Throwable) {
        Logger.d("onError", "${providerVMClass.name} onError: ${e.message}")
    }
}