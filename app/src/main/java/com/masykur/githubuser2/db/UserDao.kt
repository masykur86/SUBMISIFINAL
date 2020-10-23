package com.masykur.githubuser2.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.masykur.githubuser2.models.Item


// fungsi interface disini untuk memasukkan kueri yang akan digunakan untuk db
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertByUri(userData: Item): Long

    @Query("SELECT * FROM userDetail")
    fun getAllUsers(): LiveData<List<Item>>


    @Query("SELECT * FROM userDetail")
    fun getAllUsersByUri(): Cursor

    @Query("SELECT * FROM ${Item.TABLE_NAME} WHERE ${Item.COLUMN_ID} = :id")
    fun selectById(id: Long): Cursor

    @Delete
    suspend fun deleteUsers(userData: Item)

    @Query("DELETE FROM ${Item.TABLE_NAME} WHERE ${Item.COLUMN_ID} = :id")
    fun deleteByIdUri(id: Long): Int
}