package com.wlm.livedatademo.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wlm.livedatademo.MyApp

@Database(entities = [User::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        val instance: AppDataBase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(
                MyApp.instance,
                AppDataBase::class.java,
                "room_test_db.db"
            )
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun getUserDao(): UserDao
}