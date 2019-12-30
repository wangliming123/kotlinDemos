package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.bean.Article
import com.wlm.mvvm_wanandroid.bean.ArticleList
import com.wlm.mvvm_wanandroid.repository.KnowledgeRepository

class KnowledgeViewModel : BaseViewModel() {

    var knowledgeId : Int = 0

    val repository by lazy { KnowledgeRepository(this) }

    private val pageSize = MutableLiveData<Int>()
    private val listing = Transformations.map(pageSize) {
        repository.getList(it)
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

}