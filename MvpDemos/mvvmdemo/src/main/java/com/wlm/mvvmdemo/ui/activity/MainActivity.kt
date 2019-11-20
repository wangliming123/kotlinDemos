package com.wlm.mvvmdemo.ui.activity

import android.Manifest
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.wlm.baselib.base.BaseActivity
import com.wlm.baselib.ext.startKtxActivity
import com.wlm.mvvmdemo.R
import com.wlm.mvvmdemo.net.ApiService
import com.wlm.mvvmdemo.net.RetrofitManager
import com.wlm.mvvmdemo.repository.LoginRepository
import com.wlm.mvvmdemo.ui.fragment.HomeFragment
import com.wlm.mvvmdemo.ui.fragment.NavigationFragment
import com.wlm.mvvmdemo.ui.fragment.SquareFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity() {

    private val titleList = arrayOf("首页", "广场", "导航")
    private val fragmentList = arrayListOf<Fragment>()

    private val homeFragment by lazy { HomeFragment() }
    private val squareFragment by lazy { SquareFragment() }
    private val navigationFragment by lazy { NavigationFragment() }

    init {
        fragmentList.add(homeFragment)
        fragmentList.add(squareFragment)
        fragmentList.add(navigationFragment)
    }

    override fun layoutId() = R.layout.activity_main

    override fun initData() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
        EasyPermissions.requestPermissions(this, "应用需要访问以下权限，请允许", 0, *permissions)
    }

    override fun initView() {
        initViewPager()
        toolBarMain.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_blog -> startKtxActivity<ProjectActivity>(value = ProjectActivity.KEY_IS_BLOG to true)
                R.id.nav_project_type -> startKtxActivity<ProjectActivity>(value = ProjectActivity.KEY_IS_BLOG to false)
                R.id.nav_tool -> startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to ApiService.TOOL_URL)
                R.id.nav_collect -> switchCollect()
                R.id.nav_about -> startKtxActivity<AboutActivity>()
                R.id.nav_exit -> logout()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = titleList.size
        viewPager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = fragmentList[position]

            override fun getCount() = fragmentList.size

            override fun getPageTitle(position: Int) = titleList[position]

        }
        tabMain.setupWithViewPager(viewPager)
    }

    private fun switchCollect() {
        if (LoginRepository.isLogin) {
            startKtxActivity<MyCollectActivity>()
        } else {
            startKtxActivity<LoginActivity>()
        }
    }

    private fun logout() {
        MaterialDialog(this).show {
            title = "退出登录"
            message(text = "是否确认退出登录?")
            positiveButton(text = "确认") {
                launch(Dispatchers.Default) {
                    RetrofitManager.service.logout()
                }
                LoginRepository.isLogin = false
                LoginRepository.userJson = ""
                refreshView()
            }
            negativeButton(text = "取消")
        }
    }

    private fun refreshView() {
        navigationView.menu.findItem(R.id.nav_exit).isVisible = LoginRepository.isLogin
        homeFragment.refresh()
    }

    override fun initListener() {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            //侧滑打开监听
            override fun onDrawerOpened(drawerView: View) {
                navigationView.menu.findItem(R.id.nav_exit).isVisible = LoginRepository.isLogin
            }

        })
    }
}
