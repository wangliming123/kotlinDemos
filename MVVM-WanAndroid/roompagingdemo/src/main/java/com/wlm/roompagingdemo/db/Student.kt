package com.wlm.roompagingdemo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(@PrimaryKey(autoGenerate = true) val id: Int, val name: String)