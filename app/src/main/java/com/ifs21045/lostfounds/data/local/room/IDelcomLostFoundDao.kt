package com.ifs21045.lostfounds.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ifs21045.lostfounds.data.local.entity.DelcomLostFoundEntity
import retrofit2.http.Query


@Dao
interface IDelcomLostFoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(delcomTodo: DelcomLostFoundEntity)
    @Delete
    fun delete(delcomLostFound: DelcomLostFoundEntity)
    @androidx.room.Query("SELECT * FROM delcom_lostfounds WHERE id = :id LIMIT 1")
    fun get(id: Int): LiveData<DelcomLostFoundEntity?>
    @androidx.room.Query("SELECT * FROM delcom_lostfounds ORDER BY created_at DESC")
    fun getAllLostFounds(): LiveData<List<DelcomLostFoundEntity>?>
}