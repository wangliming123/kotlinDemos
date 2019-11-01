package com.wlm.mvvmdemo.ui

import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.wlm.baselib.base.BaseVMFragment
import com.wlm.baselib.ext.dp2xp
import com.wlm.baselib.ext.startKtxActivity
import com.wlm.baselib.view.CustomLoadMoreView
import com.wlm.baselib.view.SpaceItemDecoration
import com.wlm.mvvmdemo.R
import com.wlm.mvvmdemo.adapter.HomeArticleAdapter
import com.wlm.mvvmdemo.bean.ArticleList
import com.wlm.mvvmdemo.repository.LoginRepository
import com.wlm.mvvmdemo.util.GlideImageLoader
import com.wlm.mvvmdemo.viewmodel.HomeViewModel
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseVMFragment<HomeViewModel>() {

    override fun providerVMClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun layoutId() = R.layout.fragment_home

    private val banner by lazy { Banner(activity) }
    private val articleAdapter by lazy { HomeArticleAdapter() }
    private var currentPage = 0

    override fun initView() {
        rvHome.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(dp2xp(10)))
        }
        initBanner()
        initAdapter()

        homeRefreshLayout.run {
            setOnRefreshListener { refresh() }
            isRefreshing = true
        }
        refresh()
    }

    private fun refresh() {
        articleAdapter.setEnableLoadMore(false)
        homeRefreshLayout.isRefreshing = true
        currentPage = 0
        mViewModel.getArticleList(currentPage)
    }

    private fun initBanner() {
        banner.run {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2xp(200))
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setImageLoader(GlideImageLoader())
            setOnBannerListener { position ->

            }
        }
    }

    private fun initAdapter() {
        articleAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                //todo
            }
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            addHeaderView(banner)
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, rvHome)
        }
        rvHome.adapter = articleAdapter
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.articleStar -> {
                if (LoginRepository.isLogin) {
                    articleAdapter.run {
                        data[position].run {
                            collect = !collect
                            //todo
                        }
                        notifyDataSetChanged()
                    }
                } else {
                    activity?.startKtxActivity<LoginActivity>()
                }
            }
        }
    }

    private fun loadMore() {

    }

    override fun initData() {

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

            mArticleList.observe(this@HomeFragment, Observer { it ->
                it?.let { setArticles(it) }
            })
        }
    }

    private fun setArticles(articleList: ArticleList) {
        articleAdapter.run {
            if (homeRefreshLayout.isRefreshing) replaceData(articleList.datas)
            else addData(articleList.datas)
            setEnableLoadMore(true)
            loadMoreComplete()
        }
        homeRefreshLayout.isRefreshing = false
        currentPage++
    }

    override fun onError(e: Throwable) {

    }
    
}