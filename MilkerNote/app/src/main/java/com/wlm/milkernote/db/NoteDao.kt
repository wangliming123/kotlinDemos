package com.wlm.milkernote.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface NoteDao : BaseDao<Note> {

//    @Query("select * from Note order by id asc")
//    fun getAllNote(): DataSource.Factory<Int, Note>

    @Query("select * from Note order by updateTime desc")
    fun getAllNote(): DataSource.Factory<Int, Note>

    @Query("select * from Note where id = :id")
    fun getNote(id: Int): Note

    @Query("delete from Note where id = :id")
    fun delete(id: Int)
}