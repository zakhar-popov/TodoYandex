package com.zakhardev.todolist.todos_list.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodosDao {

    @Query("SELECT * FROM todos")
    fun observeAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE uid = :uid LIMIT 1")
    fun observeById(uid: String): Flow<TodoEntity?>

    @Query("SELECT * FROM todos WHERE uid = :uid LIMIT 1")
    suspend fun getById(uid: String): TodoEntity?

    @Query("SELECT * FROM todos")
    suspend fun getAllOnce(): List<TodoEntity>

    @Upsert
    suspend fun upsert(entity: TodoEntity)

    @Query("DELETE FROM todos WHERE uid = :uid")
    suspend fun delete(uid: String)

    @Query("DELETE FROM todos")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TodoEntity>)
}
