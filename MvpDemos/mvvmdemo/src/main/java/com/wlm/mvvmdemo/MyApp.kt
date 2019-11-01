package com.wlm.mvvmdemo

import com.google.gson.Gson
import com.wlm.baselib.base.BaseApp
import com.wlm.mvvmdemo.bean.User
import com.wlm.mvvmdemo.repository.LoginRepository

class MyApp : BaseApp() {
    companion object {
        lateinit var currentUser: User
    }

    override fun onCreate() {
        super.onCreate()
        if (LoginRepository.isLogin && LoginRepository.userJson.isNotBlank()) {
            currentUser = Gson().fromJson(LoginRepository.userJson, User::class.java)
        }
    }
}