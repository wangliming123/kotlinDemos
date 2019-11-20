package com.wlm.mvvmdemo.repository

import com.wlm.baselib.net.BaseRepository
import com.wlm.baselib.net.CustomResponse
import com.wlm.mvvmdemo.bean.ArticleList
import com.wlm.mvvmdemo.net.RetrofitManager

class SquareRepository : BaseRepository() {
    suspend fun getSquareArticleList(currentPage: Int): CustomResponse<ArticleList> {
        return apiCall { RetrofitManager.service.getSquareArticleList(currentPage) }
    }

}