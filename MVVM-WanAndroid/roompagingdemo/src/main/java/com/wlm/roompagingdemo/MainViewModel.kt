package com.wlm.roompagingdemo

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.roompagingdemo.db.StudentDb

class MainViewModel : ViewModel() {
    val dao = StudentDb.get(MyApp.instance).studentDao()
    val allStudents = LivePagedListBuilder(
        dao.getAllStudent(),
        PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)//配置分页加载的数量
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)//配置是否启动PlaceHolders
            .setInitialLoadSizeHint(PAGE_SIZE * 2)//初始化加载数量（默认3倍pagesize）
            .build()
    ).build()

    companion object {
        private const val PAGE_SIZE = 15
        private const val ENABLE_PLACEHOLDERS = false
    }
}