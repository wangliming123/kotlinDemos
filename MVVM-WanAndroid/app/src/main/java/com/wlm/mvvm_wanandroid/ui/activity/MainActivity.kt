package com.wlm.mvvm_wanandroid.ui.activity

import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseActivity
import com.wlm.mvvm_wanandroid.ui.fragment.*
import com.wlm.mvvm_wanandroid.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_main

    private val bottomTitles = arrayOf(
        R.string.str_home,
        R.string.str_knowledge_tree,
        R.string.str_navigation,
//        R.string.str_we_chat,
        R.string.str_project
    )

    private val homeFragment by lazy { HomeFragment() }
    private val knowledgeTreeFragment by lazy { KnowledgeTreeFragment() }
    private val navigationItemView by lazy { NavigationFragment() }
//    private val weChatFragment by lazy { WeChatFragment() }
    private val projectFragment by lazy { ProjectFragment() }
    private val fragmentList = arrayListOf<Fragment>()

    init {
        fragmentList.add(homeFragment)
        fragmentList.add(knowledgeTreeFragment)
        fragmentList.add(navigationItemView)
//        fragmentList.add(weChatFragment)
        fragmentList.add(projectFragment)
    }

    override fun init() {
        setSupportActionBar(toolbar)
        initDrawerLayout()
        initNav()
        initViewPager()
        initNavBottom()
    }

    private fun initDrawerLayout() {
        drawer_layout.run {
            val toggle = object : ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.app_name,
                R.string.app_name
            ) {
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    nav_view.menu.findItem(R.id.logout).isVisible = false
                }
            }
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initNav() {
        nav_view.setNavigationItemSelectedListener {
            ToastUtils.show(it.title.toString())
            when (it.itemId) {
                R.id.favorites -> {
                }
                R.id.todo -> {
                }
                R.id.night_mode -> {
                }
                R.id.setting -> {
                }
                R.id.logout -> {
                }
                R.id.about -> {
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initViewPager() {
        view_pager.run {
            offscreenPageLimit = bottomTitles.size
            adapter = object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getItem(position: Int) = fragmentList[position]

                override fun getCount() = fragmentList.size

                override fun getPageTitle(position: Int) = getString(bottomTitles[position])

            }
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    bottom_navigation.selectedItemId =
                        bottom_navigation.menu.getItem(position).itemId
                    toolbar.title = getString(if (position == 0) R.string.app_name else bottomTitles[position] )
                }

            })

        }
    }

    private fun initNavBottom() {
        bottom_navigation.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> view_pager.currentItem = 0
                    R.id.knowledge_tree -> view_pager.currentItem = 1
                    R.id.navigation -> view_pager.currentItem = 2
                    R.id.project -> view_pager.currentItem = 3
//                    R.id.we_chat -> view_pager.currentItem = 1
                }
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scan -> {
            }
            R.id.search -> {
                ToastUtils.show("to be completed")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var lastExitTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (KeyEvent.KEYCODE_BACK == keyCode) {
            val current = System.currentTimeMillis()
            if (current - lastExitTime > 2000) {
                ToastUtils.show(getString(R.string.str_exit_hint))
                lastExitTime = current
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
