package com.wlm.mvvm_wanandroid.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.bean.Article
import com.wlm.mvvm_wanandroid.bean.ArticleList
import com.wlm.mvvm_wanandroid.bean.HotKey
import com.wlm.mvvm_wanandroid.datasource.search.SearchDataSourceFactory
import com.wlm.mvvm_wanandroid.net.RetrofitManager
import com.wlm.mvvm_wanandroid.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(private val viewModel: SearchViewModel) : BaseRepository() {

    private val sourceFactory by lazy { SearchDataSourceFactory(viewModel) }


    suspend fun getHotKey(): HttpResponse<List<HotKey>> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getHotKey() }
    }

    fun getListing(pageSize: Int): Listing<Article> {
        val pagedList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.sourceLivaData.value?.invalidate() })
    }

    suspend fun queryArticles(page: Int, queryKey: String): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.queryArticles(page, queryKey) }

    }
}