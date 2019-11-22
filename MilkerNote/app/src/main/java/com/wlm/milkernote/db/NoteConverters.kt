package com.wlm.milkernote.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteConverters {

    @TypeConverter
    fun any2String(list: List<Any>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun string2Int(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {

        }.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun string2Float(value: String): List<Float> {
        val type = object : TypeToken<List<Float>>() {

        }.type
        return Gson().fromJson(value, type)
    }
}