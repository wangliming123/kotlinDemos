package com.wlm.milkernote.db

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(NoteConverters::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var content: String,
    var createTime: Long,
    var updateTime: Long
//    var starts: MutableList<Int>?,
//    var ends: MutableList<Int>?,
//    var colors: MutableList<Int>?,
//    var sizes: MutableList<Float>?
)
//{
//    constructor(
//        id: Int = 0,
//        title: String,
//        content: String,
//        createTime: Long,
//        updateTime: Long
//    ) : this(id, title, content, createTime, updateTime, null, null, null, null)
//}