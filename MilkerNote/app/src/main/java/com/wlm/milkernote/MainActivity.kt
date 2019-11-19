package com.wlm.milkernote

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import androidx.core.text.toSpannable
import com.wlm.milkernote.utils.LayoutUtils
import com.wlm.milkernote.utils.SoftHideKeyBoardUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_tools.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        SoftHideKeyBoardUtils.init(this)
    }


    private fun initView() {
        tv_text_color.setOnClickListener {

        }
        tv_text_size.setOnClickListener { }


    }
}
