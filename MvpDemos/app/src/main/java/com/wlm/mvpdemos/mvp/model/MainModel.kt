package com.wlm.mvpdemos.mvp.model

import com.wlm.mvpdemos.bean.ArticleBean
import com.wlm.mvpdemos.net.RetrofitManager
import com.wlm.mvpdemos.net.SchedulerUtils
import io.reactivex.Observable

/**
 * @author Milker
 * @since 2019.10.28
 * 主页model
 */
class MainModel {

    /**
     * 首页文章列表
     * @param page 页码
     */
    fun getArticles(page: Int): Observable<ArticleBean> {
        return RetrofitManager.service.getArticles(page).compose(SchedulerUtils.ioToMain())
    }
}