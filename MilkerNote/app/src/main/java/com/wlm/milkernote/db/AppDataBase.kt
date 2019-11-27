package com.wlm.milkernote.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wlm.milkernote.MyApp

@Database(entities = [Note::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(MyApp.instance, AppDataBase::class.java, "mvvm_wan_android.db")
                .allowMainThreadQueries().build()
        }
    }
}