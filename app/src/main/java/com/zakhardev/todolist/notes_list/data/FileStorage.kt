package com.zakhardev.todolist.notes_list.data

import android.content.Context
import com.zakhardev.todolist.notes_list.domain.TodoItem
import com.zakhardev.todolist.notes_list.domain.TodoItemJson
import com.zakhardev.todolist.notes_list.domain.TodoItemJson.json
import org.json.JSONArray
import java.io.File
import java.time.Instant
import java.util.UUID

class FileStorage(
    private val context: Context,
    private val fileName: String = "todos.json",
    private val selfDestructGraceMs: Long = 0L
) {
    private val itemsMutable = mutableListOf<TodoItem>()
    val items: List<TodoItem> get() = itemsMutable

    private fun storageFile(): File = File(context.filesDir, fileName)

    private fun shouldSelfDestruct(item: TodoItem, now: Instant = Instant.now()): Boolean {
        val dl = item.deadline ?: return false
        return now.toEpochMilli() >= dl.toEpochMilli() + selfDestructGraceMs
    }

    fun add(item: TodoItem) {
        if (shouldSelfDestruct(item)) return
        val idx = itemsMutable.indexOfFirst { it.uid == item.uid }
        if (idx >= 0) {
            itemsMutable[idx] = item
        } else {
            itemsMutable += item
        }
    }

    fun remove(uid: UUID): Boolean {
        val idx = itemsMutable.indexOfFirst { it.uid == uid }
        return if (idx >= 0) {
            itemsMutable.removeAt(idx)
            true
        } else false
    }

    fun save(): Boolean {
        val arr = JSONArray()
        itemsMutable.forEach { arr.put(it.json) }
        return runCatching {
            storageFile().writeText(arr.toString())
            true
        }.getOrElse { false }
    }

    fun load(): Boolean {
        val file = storageFile()
        if (!file.exists()) {
            itemsMutable.clear()
            return true
        }
        return runCatching {
            val text = file.readText()
            val arr = JSONArray(text)
            val loaded = mutableListOf<TodoItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val parsed = TodoItemJson.parse(obj)

                parsed?.let {
                    if (!shouldSelfDestruct(it)) {
                        loaded += parsed
                    }
                }
            }

            itemsMutable.clear()
            itemsMutable.addAll(loaded)
            true
        }.getOrElse { false }
    }
}
