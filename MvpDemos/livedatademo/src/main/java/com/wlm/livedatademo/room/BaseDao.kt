package com.wlm.livedatademo

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elements: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<T>)

    @Delete
    fun delete(element: T)

    @Delete
    fun deleteSome(vararg element: T)

    @Delete
    fun deleteList(list: List<T>)

    @Update
    fun update(element: T)


}