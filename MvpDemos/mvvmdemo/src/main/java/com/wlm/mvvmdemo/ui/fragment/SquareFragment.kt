package com.wlm.mvvmdemo.ui.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.wlm.baselib.base.BaseBindAdapter
import com.wlm.baselib.base.BaseVMFragment
import com.wlm.baselib.ext.dp2px
import com.wlm.baselib.ext.startKtxActivity
import com.wlm.baselib.utils.ToastUtils
import com.wlm.baselib.view.CustomLoadMoreView
import com.wlm.baselib.view.SpaceItemDecoration
import com.wlm.mvvmdemo.BR
import com.wlm.mvvmdemo.R
import com.wlm.mvvmdemo.bean.Article
import com.wlm.mvvmdemo.ui.activity.BrowserActivity
import com.wlm.mvvmdemo.ui.activity.LoginActivity
import com.wlm.mvvmdemo.viewmodel.SquareViewModel
import kotlinx.android.synthetic.main.fragment_square.*

class SquareFragment : BaseVMFragment<SquareViewModel>() {

    private val squareAdapter by lazy { BaseBindAdapter<Article>(R.layout.item_square, BR.article) }

    override fun providerVMClass() = SquareViewModel::class.java

    override fun layoutId() = R.layout.fragment_square

    override fun startObserve() {
        super.startObserve()
        mViewModel.squareUiState.observe(this, Observer { uiState ->
            squareRefreshLayout.isRefreshing = uiState.showLoading

            uiState.showSuccess?.let { articleList ->
                squareAdapter.run {
                    if (uiState.isRefresh) replaceData(articleList.datas)
                    else addData(articleList.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }

            if (uiState.showEnd) squareAdapter.loadMoreEnd()

            uiState.showError?.let { errorMsg ->
                ToastUtils.showShort(activity!!.applicationContext, errorMsg)
            }

            if (uiState.needLogin != null && uiState.needLogin) startKtxActivity<LoginActivity>()
        })
    }

    override fun initView() {
        initRecycleView()
        squareRefreshLayout.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        squareAdapter.setEnableLoadMore(false)
        mViewModel.getSquareArticleList(true)
    }

    private fun initRecycleView() {
        squareAdapter.run {
            setOnItemClickListener { _, _, position ->
                startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to squareAdapter.data[position].link)
            }
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, squareRecyclerView)
        }
        squareRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(squareRecyclerView.dp2px(10)))
            adapter = squareAdapter
        }
    }

    private fun loadMore() {
        mViewModel.getSquareArticleList()
    }

    override fun initData() {
        loadMore()
    }

    override fun onError(e: Throwable) {
        super.onError(e)
    }
}