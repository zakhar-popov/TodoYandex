package com.zakhardev.todolist.todos_list.data.datasourse

import com.zakhardev.todolist.todos_list.data.api.TodosApi
import com.zakhardev.todolist.todos_list.data.model.ElementRequest
import com.zakhardev.todolist.todos_list.data.model.PatchListRequest
import com.zakhardev.todolist.todos_list.data.model.toDomain
import com.zakhardev.todolist.todos_list.data.model.toDto
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import com.zakhardev.todolist.todos_list.domain.repository.TodosBackend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.math.min

class RemoteTodosBackend(
    private val api: TodosApi,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : TodosBackend {
    private val log = LoggerFactory.getLogger(RemoteTodosBackend::class.java)
    private var revision: Int = 0

    private val queue = kotlinx.coroutines.channels.Channel<suspend () -> Unit>(
        capacity = kotlinx.coroutines.channels.Channel.UNLIMITED
    )

    init {
        scope.launch {
            for (op in queue) runForever(op)
        }
    }

    override suspend fun getAll(): List<TodoItem> {
        log.info("backend.getAll called")
        val resp = api.fetchList()
        revision = resp.revision
        return resp.list.map { it.toDomain() }
    }

    override suspend fun upsert(item: TodoItem) {
        log.info("backend.upsert uid={}", item.uid)
        queue.send { upsertNetwork(item) }
    }

    override suspend fun delete(uid: String) {
        log.info("backend.delete uid={}", uid)
        queue.send { deleteNetwork(uid) }
    }

    override suspend fun patchAll(items: List<TodoItem>): List<TodoItem> {
        var delayMs = 1_000L

        while (true) {
            try {
                val resp = api.patchList(
                    revision = revision,
                    body = PatchListRequest(items.map { it.toDto() })
                )
                revision = resp.revision
                return resp.list.map { it.toDomain() }
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    val fresh = api.fetchList()
                    revision = fresh.revision
                    continue
                }
                if (!shouldRetryRead(e)) throw e
            } catch (e: Throwable) {
                if (!shouldRetryRead(e)) throw e
            }

            delay(delayMs)
            delayMs = min(delayMs * 2, 30_000L)
        }
    }

    private suspend fun upsertNetwork(item: TodoItem) {
        try {
            val resp = api.updateItem(
                revision = revision,
                id = item.uid,
                body = ElementRequest(item.toDto())
            )
            revision = resp.revision
        } catch (e: HttpException) {
            if (e.code() == 404) {
                val resp = api.addItem(revision = revision, body = ElementRequest(item.toDto()))
                revision = resp.revision
            } else if (e.code() == 400) {
                val fresh = api.fetchList()
                revision = fresh.revision
                throw e
            } else throw e
        }
    }

    private suspend fun deleteNetwork(uid: String) {
        try {
            val resp = api.deleteItem(revision = revision, id = uid)
            revision = resp.revision
        } catch (e: HttpException) {
            if (e.code() == 400) {
                val fresh = api.fetchList()
                revision = fresh.revision
                throw e
            } else throw e
        }
    }

    private suspend fun runForever(
        op: suspend () -> Unit,
        initialDelayMs: Long = 1_000L,
        maxDelayMs: Long = 30_000L,
    ) {
        var delayMs = initialDelayMs

        while (true) {
            try {
                op()
                return
            } catch (e: Throwable) {
                if (!shouldRetryModification(e)) throw e
                delay(delayMs)
                delayMs = min(delayMs * 2, maxDelayMs)
            }
        }
    }

    private fun shouldRetryRead(e: Throwable): Boolean = when (e) {
        is SocketTimeoutException -> true
        is IOException -> true
        is HttpException -> e.code() in 500..599 || e.code() == 429
        else -> false
    }

    private fun shouldRetryModification(e: Throwable): Boolean = when (e) {
        is HttpException -> shouldRetryRead(e) || e.code() == 400
        else -> shouldRetryRead(e)
    }
}