package com.zakhardev.todolist.todos_list.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE todos ADD COLUMN createdAtEpochMs INTEGER NOT NULL DEFAULT 0"
        )
    }
}