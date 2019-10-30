package com.wlm.mvpdemos.mvp.presenter

import com.wlm.mvpdemos.mvp.base.BasePresenter
import com.wlm.mvpdemos.mvp.contract.MainContract
import com.wlm.mvpdemos.mvp.model.MainModel
import com.wlm.mvpdemos.net.ExceptionHandle
import io.reactivex.disposables.Disposable

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val mainModel by lazy {
        MainModel()
    }

    override fun getArticles(page: Int) {
        checkAttached()

        mRootView?.showLoading()

        val disposable = mainModel.getArticles(page).subscribe(
            { articleBean ->
                mRootView?.apply {
                    dismissLoading()
                    if (articleBean.data != null && articleBean.errorCode == 0 && articleBean.errorMsg.isEmpty()) {
                        bindData(articleBean)
                    } else {
                        showError(articleBean.errorMsg, articleBean.errorCode)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showError(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            }
        )
        addSubscription(disposable)

    }

}