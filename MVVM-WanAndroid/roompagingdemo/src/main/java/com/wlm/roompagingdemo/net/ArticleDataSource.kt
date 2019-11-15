package com.wlm.roompagingdemo.net

import androidx.paging.ItemKeyedDataSource

class ArticleDataSource : ItemKeyedDataSource<Int, Article>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKey(item: Article): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}