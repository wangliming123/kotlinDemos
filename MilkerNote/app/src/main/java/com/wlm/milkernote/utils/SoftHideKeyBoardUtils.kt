package com.wlm.milkernote.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import com.wlm.milkernote.Weak

object SoftHideKeyBoardUtils {
    private var isFirst = true
    private var contentHeight = 0
    private var usableHeightPrevious = 0
    private lateinit var frameLayoutParams: FrameLayout.LayoutParams
    private var rootView by Weak<View> {
        null
    }

    fun init(activity: Activity) {
        //找到Activity的最外层布局控件，它其实是一个DecorView,它所用的控件就是FrameLayout
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        //获取到setContentView放进去的View
        rootView = content.getChildAt(0)
        //给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        rootView?.run {
            viewTreeObserver!!.addOnGlobalLayoutListener {
                if (isFirst) {
                    contentHeight = height //兼容华为等机型
                    isFirst = false
                }
                //当前布局发生变化时，对Activity的xml布局进行重绘
                possiblyResizeChildOfContent();
            }
            frameLayoutParams = layoutParams as FrameLayout.LayoutParams
        }
        val id = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    }

    // 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
    private fun possiblyResizeChildOfContent() {
        //获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        val usableHeightNow = computeUsableHeight()
        //如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //获取Activity中xml中布局在当前界面显示的高度
            val usableHeightSansKeyboard = rootView?.rootView!!.height
            //Activity中xml布局的高度-当前可用高度
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            //高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 键盘弹出了，Activity的xml布局高度应当减去键盘高度
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                frameLayoutParams.height = contentHeight;
            }
            //7､ 重绘Activity的xml布局
            rootView?.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val rect = Rect()
        rootView?.getWindowVisibleDisplayFrame(rect);
        // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        return (rect.bottom - rect.top)
    }
}