package com.wlm.milkernote.utils

import android.content.Context
import android.graphics.Rect
import android.view.View

object LayoutUtils {
    fun addLayoutListener(root: View, bottom: View) {
        root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            //获取root窗体可视区域
            root.getWindowVisibleDisplayFrame(rect)
            //不可视区域高度（被其他View遮挡高度）
            val rootInvisibleHeight = root.rootView.height - rect.bottom
            // 若不可视区域高度大于200，判断为打开键盘
            if (rootInvisibleHeight > 200) {
                val scrollHeight =
                    rootInvisibleHeight - (root.bottom - bottom.bottom) - getNavigationBarHeight(
                        root.context
                    )
                if (scrollHeight > 0) {
                    bottom.scrollTo(0, -scrollHeight)
                }
            } else {
                bottom.scrollTo(0, 0)
            }


        }
    }

    private fun getNavigationBarHeight(context: Context): Int {
        val id = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (id > 0 && checkDeviceHasNavigationBar(context))
            context.resources.getDimensionPixelSize(id) else 0
    }

    private fun checkDeviceHasNavigationBar(context: Context): Boolean {
        val id = context.resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0
    }
}