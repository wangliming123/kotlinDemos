package com.wlm.mvvm_wanandroid.datasource.knowledge

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.bean.Article
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeViewModel
import kotlinx.coroutines.launch

class KnowledgeDataSource(private val viewModel: KnowledgeViewModel) :
    ItemKeyedDataSource<Int, Article>() {
    private var page = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = repository.searchArticles(page, knowledgeId)

                        executeResponse(result, {
                            result.data?.let {
                                page = it.curPage
                                uiState.value = UiState(false, null, it)
                                callback.onResult(it.datas)
                            }
                        }, { msg ->
                            uiState.value = UiState(false, msg, null)
                        })
                    },
                    catchBlock = { t->
                        uiState.value = UiState(false, t.message, null)
                    },
                    handleCancellationExceptionManually = true
                )

            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        viewModel.run {
            viewModelScope.launch {
                tryCatch({
                    val result = repository.searchArticles(page, knowledgeId)
                    executeResponse(result, {
                        result.data?.let {
                            page = it.curPage
                            callback.onResult(it.datas)
                        }
                    }, {})
                }, handleCancellationExceptionManually = true)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {

    }

    override fun getKey(item: Article): Int = item.id

}