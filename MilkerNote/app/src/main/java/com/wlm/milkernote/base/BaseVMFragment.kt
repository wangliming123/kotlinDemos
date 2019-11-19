package com.wlm.milkernote.base

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.base.BaseViewModel

abstract class BaseVMFragment<VM : BaseViewModel> : BaseFragment() {
    lateinit var mViewModel: VM
    abstract val providerVMClass: Class<VM>

    override fun init() {
        initVM()
        startObserve()
    }

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