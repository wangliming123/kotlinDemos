package com.wlm.milkernote.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var content: String,
    var html: String,
    var createTime: Long,
    var updateTime: Long
)