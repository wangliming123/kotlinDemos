package com.wlm.milkernote.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.*
import android.text.style.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.orhanobut.logger.Logger
import com.wlm.milkernote.R
import com.wlm.milkernote.utils.HtmlUtils
import com.wlm.milkernote.utils.SoftKeyBoardUtils
import com.wlm.milkernote.delegate.SharedPrefs
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
            if (changeByHtml) {
                changeByHtml = false
                return
            }
            if (addImage) {
                addImage = false
                return
            }
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
            Logger.d("onTextChanged: s: $s, start: $start, before: $before,  count: $count")
        }
    }


    @ColorInt
    var color: Int = Color.parseColor("#000000")

    var relativeSize: Float by SharedPrefs(RELATIVE_SIZE_KEY, 1.0f)

    private var changeByHtml = false//外部传入html改变富文本
    private var addImage = false//插入图片

    var text: String = ""
        get() = et_text.text.toString()
        private set

    fun getSavedHtml(): String {
        return HtmlUtils.toHtml(et_text.text)
    }

    fun setHtml(html: String, content: String) {
        val spannableString = HtmlUtils.fromHtml(context, html, content)
        changeByHtml = true
        et_text.setText(spannableString)
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

    fun addImage(bitmap: Bitmap) {
        addImage = true
        val spannableString = SpannableString("[图片]")
        ImageSpan(context, bitmap, ImageSpan.ALIGN_BASELINE).source
        spannableString.setSpan(
            ImageSpan(context, bitmap, ImageSpan.ALIGN_BASELINE),
            0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val index = et_text.selectionStart
        if (index < 0 || index > text.length) {
            et_text.append(spannableString)
        }else {
            et_text.text.insert(index, spannableString)
        }

    }

}