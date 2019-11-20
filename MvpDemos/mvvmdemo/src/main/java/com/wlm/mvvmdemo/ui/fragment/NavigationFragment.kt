package com.wlm.mvvmdemo.ui.fragment

import com.wlm.baselib.base.BaseVMFragment
import com.wlm.mvvmdemo.R
import com.wlm.mvvmdemo.viewmodel.NavigationViewModel

class NavigationFragment : BaseVMFragment<NavigationViewModel>() {

    override fun providerVMClass() = NavigationViewModel::class.java

    override fun layoutId() = R.layout.fragment_navigation

    override fun initView() {

    }

    override fun initData() {

    }

}