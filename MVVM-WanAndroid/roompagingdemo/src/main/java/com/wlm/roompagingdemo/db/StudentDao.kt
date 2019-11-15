package com.wlm.roompagingdemo.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {

    @Query("select * from Student order by name collate nocase asc")
    fun getAllStudent(): DataSource.Factory<Int, Student>

    @Insert
    fun insert(students: List<Student>)
}