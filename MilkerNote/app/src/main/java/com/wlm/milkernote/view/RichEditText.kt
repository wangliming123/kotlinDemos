package com.wlm.milkernote.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.orhanobut.logger.Logger
import com.wlm.milkernote.R
import com.wlm.milkernote.utils.SoftKeyBoardUtils
import com.wlm.mvvm_wanandroid.utils.SharedPrefs
import kotlinx.android.synthetic.main.layout_rich_edit_text.view.*
import kotlinx.android.synthetic.main.layout_tools.view.*

class RichEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val RELATIVE_SIZE_KEY = "RELATIVE_SIZE_KEY"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    private val textWatcher = object : TextWatcher {
        var start = 0
        var end = 0
        override fun afterTextChanged(s: Editable?) {
            s?.run {
                setSpan(
                    RelativeSizeSpan(relativeSize), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            this.start = start
            this.end = start + count
            if (before > 0) {
                changeText(before)
            }else {
                starts.add(start)
                ends.add(end)
                colorList.add(color)
                relativeSizeList.add(relativeSize)
            }
            Logger.d("onTextChanged: s: $s, start: $start, before: $before,  count: $count")
        }
    }

    private fun changeText(before: Int) {
        if (before > 0) {
            val lastLength = ends[ends.size - 1] - starts[starts.size - 1]
            if (before >= lastLength) {
                starts.removeAt(starts.size - 1)
                ends.removeAt(ends.size - 1)
                colorList.removeAt(colorList.size - 1)
                relativeSizeList.removeAt(relativeSizeList.size - 1)
                changeText(before - lastLength)
            }else {
                ends[ends.size - 1] -= before
            }
        }
    }

    private val colorList = mutableListOf<Int>()
    private val relativeSizeList = mutableListOf<Float>()
    private val starts = mutableListOf<Int>()
    private val ends = mutableListOf<Int>()

    @ColorInt
    var color: Int = Color.parseColor("#000000")

    var relativeSize: Float by SharedPrefs(RELATIVE_SIZE_KEY, 1.0f)

    var text: String
        get() = et_text.text.toString()
        set(value) {
            et_text.setText(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_rich_edit_text, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RichEditText)
        if (relativeSize == 1.0f)
            relativeSize = typedArray.getFloat(R.styleable.RichEditText_relativeSize, 1.2f)
        color = typedArray.getColor(R.styleable.RichEditText_richColor, Color.BLACK)
        typedArray.recycle()

        initData()

        initView()
        et_text.addTextChangedListener(textWatcher)
    }

    private fun initData() {
        tv_text_size.text = (relativeSize * 10).toInt().toString()
        @Suppress("DEPRECATION")
        rg_text_color.check(
            when (color) {
                context.resources.getColor(R.color.black) -> R.id.text_color_black
                context.resources.getColor(R.color.red) -> R.id.text_color_red
                context.resources.getColor(R.color.green) -> R.id.text_color_green
                context.resources.getColor(R.color.blue) -> R.id.text_color_blue
                context.resources.getColor(R.color.orange) -> R.id.text_color_orange
                else -> R.id.text_color_default
            }
        )

    }

    private fun initView() {
        et_text.setOnClickListener {
            ll_text_tool.visibility = View.GONE
        }
        et_text.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) tools.visibility = View.VISIBLE
        }

        tv_tool_text.setOnClickListener {
            when (ll_text_tool.visibility) {
                View.VISIBLE -> {
                    ll_text_tool.visibility = View.GONE
                    if (et_text.isFocusable) {
                        SoftKeyBoardUtils.showSoftKeyboard(et_text)
                    }
                }
                View.GONE -> {
                    ll_text_tool.visibility = View.VISIBLE
                    SoftKeyBoardUtils.hideSoftKeyboard(et_text)
                }
            }
        }
        tv_text_size_add.setOnClickListener {
            relativeSize += 0.1f
            tv_text_size.text = (relativeSize * 10).toInt().toString()
        }
        tv_text_size_sub.setOnClickListener {
            relativeSize -= 0.1f
            tv_text_size.text = (relativeSize * 10).toInt().toString()
        }
        rg_text_color.setOnCheckedChangeListener { _, checkedId ->
            @Suppress("DEPRECATION")
            color = when (checkedId) {
                R.id.text_color_black -> context.resources.getColor(R.color.black)
                R.id.text_color_red -> context.resources.getColor(R.color.red)
                R.id.text_color_green -> context.resources.getColor(R.color.green)
                R.id.text_color_blue -> context.resources.getColor(R.color.blue)
                R.id.text_color_orange -> context.resources.getColor(R.color.orange)
                else -> context.resources.getColor(R.color.text_color_normal)
            }
        }
    }

    fun hideTools() {
        tools.visibility = View.GONE
    }


}