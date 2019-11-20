package com.wlm.mvvmdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.base.BaseViewModel
import com.wlm.baselib.net.executeResponse
import com.wlm.mvvmdemo.bean.ArticleList
import com.wlm.mvvmdemo.repository.SquareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SquareViewModel : BaseViewModel() {
    private val squareRepository by lazy { SquareRepository() }

    private var currentPage = 0

    val squareUiState = MutableLiveData<SquareUiState>()

    fun getSquareArticleList(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) { emitUiState(showLoading = true) }
            if (isRefresh) currentPage = 0
            tryCatch({
                val result = squareRepository.getSquareArticleList(currentPage)
                withContext(Dispatchers.Main) {
                    executeResponse(result, {
                        val articleList = result.data
                        if (articleList.offset >= articleList.total) {
                            emitUiState(showEnd = true)
                            return@executeResponse
                        }
                        currentPage++
                        emitUiState(showSuccess = articleList, isRefresh = isRefresh)

                    }, {
                        emitUiState(showError = result.errorMsg)
                    })
                }
            }, handleCancellationExceptionManually = true)
        }
    }

    private fun emitUiState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: ArticleList? = null,
        showEnd: Boolean = true,
        isRefresh: Boolean = false,
        needLogin: Boolean? = null
    ) {
        squareUiState.value =
            SquareUiState(showLoading, showError, showSuccess, showEnd, isRefresh, needLogin)
    }

    data class SquareUiState(
        val showLoading: Boolean,
        val showError: String?,
        val showSuccess: ArticleList?,
        val showEnd: Boolean,
        val isRefresh: Boolean,
        val needLogin: Boolean? = null
    )
}

