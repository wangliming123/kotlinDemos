package com.wlm.mvvmdemo.ui

import androidx.fragment.app.Fragment
import com.wlm.baselib.base.BaseActivity
import com.wlm.mvvmdemo.R

class MainActivity : BaseActivity() {

    private val titleList = arrayOf("首页", "广场")
    private val fragmentList = arrayListOf<Fragment>()

    private val homeFragment by lazy { HomeFragment() }

    init {
        fragmentList.add(homeFragment)
    }

    override fun layoutId() = R.layout.activity_main

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initListener() {

    }
}
