package com.wlm.milkernote

import com.wlm.milkernote.base.BaseVMActivity
import com.wlm.milkernote.utils.SoftHideKeyBoardUtils
import com.wlm.milkernote.viewmodel.CreateViewModel
import kotlinx.android.synthetic.main.layout_tools.*

class CreateActivity : BaseVMActivity<CreateViewModel>() {
    override val providerVMClass: Class<CreateViewModel> = CreateViewModel::class.java
    override val layoutId: Int = R.layout.activity_create


    override fun init() {
        super.init()
        initView()

        SoftHideKeyBoardUtils.init(this)
    }

    private fun initView() {
        tv_text_color.setOnClickListener {

        }
        tv_text_size.setOnClickListener { }


    }
}
