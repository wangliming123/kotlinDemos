package com.wlm.milkernote

import android.Manifest
import androidx.lifecycle.Observer
import com.wlm.milkernote.adapter.NoteAdapter
import com.wlm.milkernote.base.BaseVMActivity
import com.wlm.milkernote.ext.startKtxActivity
import com.wlm.milkernote.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseVMActivity<MainViewModel>() {
    override val providerVMClass: Class<MainViewModel> = MainViewModel::class.java
    override val layoutId: Int = R.layout.activity_main

    private val adapter by lazy { NoteAdapter() }
    override fun init() {
        super.init()

        initView()
        initData()
    }

    private fun initData() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        EasyPermissions.requestPermissions(this, "应用需要访问以下权限，请允许", 0, *permissions)
    }

    private fun initView() {
        setSupportActionBar(main_tool)
        rv_note.adapter = adapter
        fab_add_note.setOnClickListener {
            startKtxActivity<CreateActivity>()
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.allNote.observe(this, Observer {
            adapter.submitList(it)
        })
    }

}
