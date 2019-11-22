package com.wlm.milkernote.viewmodel

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.milkernote.db.AppDataBase
import com.wlm.mvvm_wanandroid.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    private val dao by lazy { AppDataBase.instance.getNoteDao() }
    val allNote = LivePagedListBuilder(dao.getAllNote(), PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
        .setInitialLoadSizeHint(PAGE_SIZE * 2)
        .build()).build()


    companion object {
        private const val PAGE_SIZE = 10
        private const val ENABLE_PLACEHOLDERS = false
    }
}