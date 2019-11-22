package com.wlm.milkernote.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.wlm.milkernote.R
import kotlinx.android.synthetic.main.layout_tools.view.*

class ToolView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    private val mContext: Context = context

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_tools, this)

        initView()
    }

    private fun initView() {
        tv_tool_text.setOnClickListener {
            ll_text_tool.visibility = when(ll_text_tool.visibility) {
                View.VISIBLE -> View.GONE
                View.GONE -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}