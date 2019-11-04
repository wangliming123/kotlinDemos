package com.wlm.mvvmdemo.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.wlm.baselib.base.BaseActivity
import com.wlm.mvvmdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val titleList = arrayOf("首页")
    private val fragmentList = arrayListOf<Fragment>()

    private val homeFragment by lazy { HomeFragment() }

    init {
        fragmentList.add(homeFragment)
    }

    override fun layoutId() = R.layout.activity_main

    override fun initData() {

    }

    override fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = titleList.size
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = fragmentList[position]

            override fun getCount() = fragmentList.size

            override fun getPageTitle(position: Int) = titleList[position]

        }
        tabMain.setupWithViewPager(viewPager)
    }

    override fun initListener() {

    }
}
