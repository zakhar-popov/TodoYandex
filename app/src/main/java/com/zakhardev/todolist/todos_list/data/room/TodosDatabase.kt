package com.zakhardev.todolist.todos_list.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TodoEntity::class],
    version = 2,
    exportSchema = true
)
abstract class TodosDatabase : RoomDatabase() {
    abstract fun todosDao(): TodosDao
}
