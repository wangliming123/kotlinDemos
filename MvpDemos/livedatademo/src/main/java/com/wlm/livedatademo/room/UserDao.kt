package com.wlm.livedatademo.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao : BaseDao<User> {

    @Query("select * from User")
    fun queryAll(): List<User>

    @Query("select * from User where id = :id")
    fun query(id: Int): User

    @Query("select * from User where userName = :userName")
    fun query(userName: String): User

    @Query("select * from User order by id desc")
    fun queryDesc(): List<User>

    @Query("delete from User")
    fun deleteAll()

}