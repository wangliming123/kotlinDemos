package com.wlm.mvvmdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.baselib.base.BaseViewModel
import com.wlm.baselib.net.RequestResult
import com.wlm.mvvmdemo.bean.User
import com.wlm.mvvmdemo.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : BaseViewModel() {

    val uiState = MutableLiveData<LoginUiState>()

    private val loginRepository by lazy { LoginRepository() }

    /**
     * @param userName 用户名
     * @param password 密码
     *
     * 登录
     */
    fun login(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (userName.isBlank() || password.isBlank()) return@launch

            withContext(Dispatchers.Main) { showLoading() }

            //ViewModel只处理视图逻辑，数据仓库Repository负责业务逻辑
            val result = loginRepository.login(userName, password)
            withContext(Dispatchers.Main) {
                if (result is RequestResult.Success) {
                    showSuccess(result.data)
                } else if (result is RequestResult.Error) {
                    showError(result.exception.message)
                }
            }
        }
    }

    fun register(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (userName.isBlank() || password.isBlank()) return@launch

            withContext(Dispatchers.Main) { showLoading() }

            val result = loginRepository.register(userName, password)
            withContext(Dispatchers.Main) {
                if (result is RequestResult.Success) {
                    showSuccess(result.data)
                } else if (result is RequestResult.Error) {
                    showError(result.exception.message)
                }
            }
        }
    }

    private fun showLoading() {
        emitUiState(showProgress = true)
    }

    private fun showSuccess(user: User) {
        emitUiState(showSuccess = user)
    }

    private fun showError(message: String?) {
        emitUiState(showError = message)
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        showSuccess: User? = null
    ) {
        val loginUiState = LoginUiState(
            showProgress,
            showError,
            showSuccess
        )
        uiState.value = loginUiState
    }

    data class LoginUiState(
        val showProgress: Boolean,
        val showError: String?,
        val showSuccess: User?
    )

}