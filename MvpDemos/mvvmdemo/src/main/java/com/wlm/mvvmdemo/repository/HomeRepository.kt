package com.wlm.mvvmdemo.repository

import com.wlm.baselib.net.BaseRepository
import com.wlm.baselib.net.CustomResponse
import com.wlm.mvvmdemo.bean.ArticleList
import com.wlm.mvvmdemo.net.RetrofitManager

class HomeRepository : BaseRepository() {
    suspend fun getArticleList(page: Int): CustomResponse<ArticleList> {
        return apiCall { RetrofitManager.service.getArticles(page) }
    }

}