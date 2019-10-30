package com.wlm.livedatademo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "userName")
    var userName: String,
    @ColumnInfo(name = "nickName")
    var nickName: String,
    @ColumnInfo(name = "password")
    var password: String
)