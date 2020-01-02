package com.wlm.mvvm_wanandroid.ui.fragment

import android.graphics.Color
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.DefaultArticleAdapter
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.bean.Knowledge
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeViewModel
import kotlinx.android.synthetic.main.refresh_layout.*
import kotlinx.android.synthetic.main.refresh_layout.multiple_status_view

class KnowledgeFragment(private val knowledge: Knowledge, private val type: Int = TYPE_KNOWLEDGE) : BaseVMFragment<KnowledgeViewModel>(){

    companion object {
        const val TYPE_KNOWLEDGE = 0
        const val TYPE_PROJECT = 1
        const val TYPE_WX_ARTICLE = 2
    }
    override val providerVMClass: Class<KnowledgeViewModel> = KnowledgeViewModel::class.java

    override val layoutId: Int = R.layout.refresh_layout

    private val adapter by lazy { DefaultArticleAdapter() }

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    override fun init() {
        super.init()
        mViewModel.knowledgeId = knowledge.id
        mViewModel.type = type

        rv_refresh.adapter = adapter

        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@KnowledgeFragment, Observer {
                adapter.submitList(it)
            })
            uiState.observe(this@KnowledgeFragment, Observer { state ->
                layout_refresh.isRefreshing = state.loading

                if (state.loading) {
                    if (isRefreshFromPull) {
                        isRefreshFromPull = false
                    } else {
                        multipleStatusView?.showLoading()
                    }
                }

                state.success?.let {
                    if (it.datas.isEmpty()) {
                        multipleStatusView?.showEmpty()
                    }else {
                        multipleStatusView?.showContent()
                    }
                }

                state.error?.let {
                    multipleStatusView?.showError()
                    Logger.d("load_error", it)
                }
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }
}