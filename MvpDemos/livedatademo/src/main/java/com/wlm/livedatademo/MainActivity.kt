package com.wlm.livedatademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wlm.livedatademo.room.AppDataBase
import com.wlm.livedatademo.room.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val userViewModel by lazy {
        //        ViewModelProviders.of(this).get(UserViewModel::class.java)
        ViewModelProvider.AndroidViewModelFactory(application).create(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userViewModel.userName.value = etUserName.text.toString()
                userViewModel.user.value = userViewModel.user.value?.apply {
                    userName = etUserName.text.toString()
                }
            }
        })

        etNickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userViewModel.nickName.value = etNickName.text.toString()
                userViewModel.user.value = userViewModel.user.value?.apply {
                    nickName = etNickName.text.toString()
                }
            }
        })
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userViewModel.password.value = etPassword.text.toString()
                userViewModel.user.value = userViewModel.user.value?.apply {
                    password = etPassword.text.toString()
                }
            }
        })

        btnSave.setOnClickListener {
            val dao = AppDataBase.instance.getUserDao()
            userViewModel.user.value = userViewModel.user.value?.apply {
                userName = etUserName.text.toString()
                nickName = etNickName.text.toString()
                password = etPassword.text.toString()
            }?:User(
                1,
                etUserName.text.toString(),
                etNickName.text.toString(),
                etPassword.text.toString()
            )
            dao.insert(userViewModel.user.value!!)
        }

        userViewModel.userName.observe(this, Observer {
            tvUserName.text = it
        })
        userViewModel.nickName.observe(this, Observer {
            tvNickName.text = it
        })
        userViewModel.password.observe(this, Observer {
            tvPassword.text = it
        })
        userViewModel.user.observe(this, Observer {
            tvUser.text = it?.toString()
        })

    }
}
