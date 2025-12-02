package com.zakhardev.todolist.todos_list.domain

import android.graphics.Color
import com.zakhardev.todolist.todos_list.domain.model.Importance
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import org.json.JSONObject
import java.time.Instant
import java.util.UUID

object TodoItemJson {
    fun parse(json: JSONObject): TodoItem? {
        val uid = json.optString("uid", UUID.randomUUID().toString())
        val text = json.optString("text", null) ?: return null
        val importance = json.optString("importance", "NORMAL").let { s ->
            when (s.uppercase()) {
                "LOW" -> Importance.LOW
                "HIGH" -> Importance.HIGH
                else -> Importance.NORMAL
            }
        }

        val color = if (json.has("color")) json.optInt("color", Color.WHITE) else Color.WHITE

        val deadline = if (json.has("deadline")) {
            val ms = json.optLong("deadline", -1L)
            if (ms > 0) Instant.ofEpochMilli(ms) else null
        } else null

        val isDone = json.optBoolean("isDone", false)

        return TodoItem(
            uid = uid,
            text = text,
            importance = importance,
            color = color,
            deadline = deadline,
            isDone = isDone
        )
    }

    val TodoItem.json: JSONObject
        get() {
            val obj = JSONObject()
            obj.put("uid", uid)
            obj.put("text", text)
            if (importance != Importance.NORMAL) {
                obj.put("importance", importance.name)
            }
            if (color != Color.WHITE) {
                obj.put("color", color)
            }
            deadline?.let { obj.put("deadline", it.toEpochMilli()) }
            obj.put("isDone", isDone)
            return obj
        }
}