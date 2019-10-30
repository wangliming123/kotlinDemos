package com.wlm.livedatademo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextViewModel : ViewModel(){

    val text = MutableLiveData<String>()


}