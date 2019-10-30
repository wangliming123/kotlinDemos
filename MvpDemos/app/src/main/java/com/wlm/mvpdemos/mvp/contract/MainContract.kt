package com.wlm.mvpdemos.mvp.contract

import com.wlm.mvpdemos.bean.ArticleBean
import com.wlm.mvpdemos.mvp.base.IBaseView

interface MainContract {
    interface View : IBaseView {
        fun showError(msg: String,errorCode:Int)
        fun showToast(any: Any)
        fun bindData(articleBean: ArticleBean)
    }

    interface Presenter {
        fun getArticles(page: Int)
    }
}