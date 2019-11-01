package com.wlm.mvvmdemo.repository

import com.google.gson.Gson
import com.wlm.baselib.net.BaseRepository
import com.wlm.baselib.net.RequestResult
import com.wlm.baselib.utils.PreferenceUtils
import com.wlm.mvvmdemo.MyApp
import com.wlm.mvvmdemo.bean.User
import com.wlm.mvvmdemo.net.RetrofitManager
import java.io.IOException

class LoginRepository : BaseRepository() {

    companion object {
        private const val IS_LOGIN ="is_login"
        private const val USER_JSON ="user_json"
        var isLogin: Boolean by PreferenceUtils(IS_LOGIN, false)
        var userJson: String by PreferenceUtils(USER_JSON, "")
    }


    suspend fun login(userName: String, password: String): RequestResult<User> {
        return saveApiCall(call = { requestLogin(userName, password) }, errorMessage = "登录失败")
    }

    private suspend fun requestLogin(userName: String, password: String): RequestResult<User> {
        val response = RetrofitManager.service.login(userName, password)
        return if (response.errorCode != -1) {
            val user = response.data
            isLogin = true
            userJson = Gson().toJson(user)
            MyApp.currentUser = user
            RequestResult.Success(user)
        }else {
            RequestResult.Error(IOException(response.errorMsg))
        }
    }

    suspend fun register(userName: String, password: String): RequestResult<User> {
        return saveApiCall({ requestRegister(userName, password) }, "注册失败")
    }

    private suspend fun requestRegister(userName: String, password: String): RequestResult<User> {
        val response = RetrofitManager.service.register(userName, password, password)
        return if (response.errorCode != -1) {
            requestLogin(userName, password)
        }else {
            RequestResult.Error(IOException(response.errorMsg))
        }
    }


}