package com.wlm.livedatademo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wlm.livedatademo.room.User

class UserViewModel : ViewModel(){

    val userName = MutableLiveData<String>()
    val nickName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val user = MutableLiveData<User?>()


}