package com.wlm.mvvm_wanandroid.bean

import java.io.Serializable

data class Knowledge(
    val children: List<Knowledge>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
): Serializable