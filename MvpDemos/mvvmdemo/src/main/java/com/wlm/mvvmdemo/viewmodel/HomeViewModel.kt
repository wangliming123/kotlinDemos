package com.wlm.mvvmdemo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.base.BaseViewModel
import com.wlm.baselib.net.executeResponse
import com.wlm.mvvmdemo.bean.ArticleList
import com.wlm.mvvmdemo.bean.BannerData
import com.wlm.mvvmdemo.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : BaseViewModel() {
    private val homeRepository by lazy { HomeRepository() }
    val mArticleList: MutableLiveData<ArticleList> = MutableLiveData()
    val mBannerList: MutableLiveData<List<BannerData>> = MutableLiveData()
    fun getArticleList(page: Int) {
        viewModelScope.launch {
            tryCatch(
                {
                    val result =
                        withContext(Dispatchers.IO) { homeRepository.getArticleList(page) }
                    executeResponse(result, { mArticleList.value = result.data }, {})
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun getBanners() {
        viewModelScope.launch {
            tryCatch(
                {
                    val result = withContext(Dispatchers.IO) { homeRepository.getBanners() }
                    executeResponse(result, { mBannerList.value = result.data }, {})
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun collectArticle(id: Int, collect: Boolean) {
        viewModelScope.launch {
            tryCatch(
                {
                    val result = withContext(Dispatchers.IO) {
                        if (collect)  homeRepository.collectArticle(id)
                        else homeRepository.unCollectArticle(id)
                    }
                    Log.d("articleList", result.toString())
                },
                handleCancellationExceptionManually = true
            )
        }
    }


}