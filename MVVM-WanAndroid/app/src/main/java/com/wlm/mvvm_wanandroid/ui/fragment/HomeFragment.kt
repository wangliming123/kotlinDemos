package com.wlm.mvvm_wanandroid.ui.fragment

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.HomeAdapter
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseVMFragment<HomeViewModel>() {
    override val layoutId = R.layout.fragment_home
    override val providerVMClass = HomeViewModel::class.java

    private val adapter by lazy { HomeAdapter() }

    private var isRefreshFromPull = false

    override fun init() {
        super.init()

        rv_home.adapter = adapter

        refresh_layout.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.BLUE)
        refresh_layout.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@HomeFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@HomeFragment, Observer {
                refresh_layout.isRefreshing = it.loading

                it.success?.let {

                }

                it.error?.let {

                }
            })

            initLoad()
        }
    }
}