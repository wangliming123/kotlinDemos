package com.wlm.milkernote

import androidx.lifecycle.Observer
import com.wlm.milkernote.adapter.NoteAdapter
import com.wlm.milkernote.base.BaseVMActivity
import com.wlm.milkernote.ext.dp2px
import com.wlm.milkernote.ext.startKtxActivity
import com.wlm.milkernote.view.SpaceItemDecoration
import com.wlm.milkernote.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseVMActivity<MainViewModel>() {
    override val providerVMClass: Class<MainViewModel> = MainViewModel::class.java
    override val layoutId: Int = R.layout.activity_main

    private val adapter by lazy { NoteAdapter() }
    override fun init() {
        super.init()
        rv_note.adapter = adapter
        rv_note.addItemDecoration(SpaceItemDecoration(dp2px(10)))

        initView()

    }

    private fun initView() {
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
