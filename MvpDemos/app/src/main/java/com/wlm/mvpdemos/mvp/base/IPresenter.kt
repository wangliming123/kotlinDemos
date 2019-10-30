package com.wlm.mvpdemos.mvp.base

/**
 * @author Milker
 * @since 2019.10.21
 * Presenter基类
 */
interface IPresenter<in V : IBaseView> {
    fun attachView(mRootView: V)
    fun detachView()
}