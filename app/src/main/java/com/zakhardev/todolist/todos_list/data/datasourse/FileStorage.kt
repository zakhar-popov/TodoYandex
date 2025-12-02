package com.zakhardev.todolist.todos_list.data.datasourse

import android.content.Context
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import com.zakhardev.todolist.todos_list.domain.TodoItemJson
import com.zakhardev.todolist.todos_list.domain.TodoItemJson.json
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Instant

class FileStorage(
    private val context: Context,
    private val fileName: String = "todos.json",
    private val selfDestructGraceMs: Long = 0L
) {
    private val log = LoggerFactory.getLogger(FileStorage::class.java)

    private val itemsMutable = mutableListOf<TodoItem>()
    val items: List<TodoItem> get() = itemsMutable

    private fun storageFile(): File = File(context.filesDir, fileName)

    private fun shouldSelfDestruct(item: TodoItem, now: Instant = Instant.now()): Boolean {
        val dl = item.deadline ?: return false
        return now.toEpochMilli() >= dl.toEpochMilli() + selfDestructGraceMs
    }

    fun add(item: TodoItem) {
        log.info("add uid={} done={} imp={} textLen={}", item.uid, item.isDone, item.importance, item.text.length)

        if (shouldSelfDestruct(item)) return
        val idx = itemsMutable.indexOfFirst { it.uid == item.uid }
        if (idx >= 0) {
            itemsMutable[idx] = item
            log.debug("add replace uid={} index={} size={}", item.uid, idx, itemsMutable.size)
        } else {
            itemsMutable += item
            log.debug("add insert uid={} size={}", item.uid, itemsMutable.size)
        }
    }

    fun remove(uid: String): Boolean {
        log.info("remove uid={}", uid)

        val idx = itemsMutable.indexOfFirst { it.uid == uid }
        return if (idx >= 0) {
            itemsMutable.removeAt(idx)
            log.debug("remove ok uid={} index={} size={}", uid, idx, itemsMutable.size)
            true
        } else {
            log.warn("remove miss uid={} reason=not_found", uid)
            false
        }
    }

    fun save(): Boolean {
        log.info("save start file='{}' count={}", storageFile().absolutePath, itemsMutable.size)

        val arr = JSONArray()
        itemsMutable.forEach { arr.put(it.json) }
        return runCatching {
            storageFile().writeText(arr.toString())
            true
        }.getOrElse { e ->
            log.error("save failed file='{}'", storageFile().absolutePath, e)
            false
        }
    }

    fun load(): Boolean {
        val file = storageFile()
        log.info("load start file='{}' exists={}", file.absolutePath, file.exists())

        if (!file.exists()) {
            itemsMutable.clear()
            log.debug("load no file cleared size={}", itemsMutable.size)
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
        }.getOrElse { e ->
            log.error("load failed file='{}'", file.absolutePath, e)
            false
        }
    }
}