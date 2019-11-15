package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.bean.Article
import com.wlm.mvvm_wanandroid.bean.ArticleList
import com.wlm.mvvm_wanandroid.repository.HomeRepository

class HomeViewModel : BaseViewModel() {

    val homeRepository by lazy { HomeRepository(this) }

//    val dao by lazy { AppDataBase.instance.getArticleDao() }

    private val pageSize = MutableLiveData<Int>()

    private val result = Transformations.map(pageSize) {
        homeRepository.getListingData(it)
    }

    val pagedList = Transformations.switchMap(result) { it.pagedList }


    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        result.value?.refresh?.invoke()
    }


}