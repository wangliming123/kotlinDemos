package com.wlm.mvvm_wanandroid.datasource.navigation

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.bean.Navigation
import com.wlm.mvvm_wanandroid.viewmodel.NavigationViewModel

class NavigationDataSourceFactory(private val viewModel: NavigationViewModel) : BaseDataSourceFactory<NavigationDataSource, Navigation>() {

    override fun createDataSource(): NavigationDataSource = NavigationDataSource(viewModel)

}