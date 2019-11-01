package com.wlm.baselib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

abstract class BaseVMFragment<VM : BaseViewModel> : Fragment() {

    lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
        startObserve()
        initView()
        initData()
    }

    private fun initVM() {
        mViewModel = ViewModelProviders.of(this).get(providerVMClass())
        lifecycle.addObserver(mViewModel)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(mViewModel)
        super.onDestroy()
    }

    open fun startObserve() {
        mViewModel.mException.observe(this, Observer { onError(it) })
    }

    abstract fun onError(e: Throwable)

    /**
     * Activity对应ViewModel类
     */
    abstract fun providerVMClass(): Class<VM>

    /**
     * 布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 初始化数据
     */
    abstract fun initData()

}