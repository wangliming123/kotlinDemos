package com.wlm.mvvmdemo.viewmodel

import com.wlm.baselib.base.BaseViewModel
import com.wlm.mvvmdemo.repository.NavigationRepository

class NavigationViewModel : BaseViewModel() {

    private val navigationRepository by lazy { NavigationRepository() }

}