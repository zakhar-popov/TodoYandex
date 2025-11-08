package com.zakhardev.todolist

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zakhardev.todolist.ui.theme.TodoListTheme
import com.zakhardev.todolist.notes_list.data.FileStorage
import com.zakhardev.todolist.notes_list.domain.Importance
import com.zakhardev.todolist.notes_list.domain.TodoItem
import org.slf4j.LoggerFactory
import java.time.Instant
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val log = LoggerFactory.getLogger(MainActivity::class.java)

    // Leak simulator: держит ссылку на Activity, чтобы LeakCanary её поймал
    object ActivityHolder {
        var ref: MainActivity? = null
    }

    private lateinit var storage: FileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        storage = FileStorage(applicationContext, "todos.json")

        setContent {
            TodoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestScreen(
                        onSmall = { runSmall() },
                        onBig = { runBig() },
                        onLeak = {
                            ActivityHolder.ref = this // симуляция утечки (плохо)
                            log.warn("Leak simulated: Activity stored in singleton")
                        },
                        onFixLeak = {
                            ActivityHolder.ref = null // исправление
                            log.info("Leak fixed: Activity released from singleton")
                        },
                        modifier = Modifier.padding(innerPadding).padding(16.dp)
                    )
                }
            }
        }
    }

    private fun runSmall() {
        val items = generateItems(100)
        storage.load()
        items.forEach { storage.add(it) }

        val t0 = SystemClock.elapsedRealtime()
        val okSave = storage.save()
        val t1 = SystemClock.elapsedRealtime()
        val okLoad = storage.load()
        val t2 = SystemClock.elapsedRealtime()

        log.info("SMALL saveOk={} loadOk={} saveMs={} loadMs={} count={}",
            okSave, okLoad, (t1 - t0), (t2 - t1), storage.items.size)
    }

    private fun runBig() {
        val items = generateItems(20_000)
        storage.load()
        items.forEach { storage.add(it) }

        val t0 = SystemClock.elapsedRealtime()
        val okSave = storage.save()
        val t1 = SystemClock.elapsedRealtime()
        val okLoad = storage.load()
        val t2 = SystemClock.elapsedRealtime()

        log.info("BIG saveOk={} loadOk={} saveMs={} loadMs={} count={}",
            okSave, okLoad, (t1 - t0), (t2 - t1), storage.items.size)
    }

    private fun generateItems(n: Int): List<TodoItem> {
        val now = Instant.now()
        return List(n) { i ->
            TodoItem(
                text = "Item $i - ${randomText()}",
                importance = when (i % 3) {
                    0 -> Importance.LOW
                    1 -> Importance.NORMAL
                    else -> Importance.HIGH
                },
                deadline = if (i % 5 == 0) now.plusSeconds((i % 120).toLong()) else null,
                isDone = (i % 7 == 0)
            )
        }
    }

    private fun randomText(): String {
        val letters = "abcdefghijklmnopqrstuvwxyz"
        return (1..16).joinToString("") { letters[Random.nextInt(letters.length)].toString() }
    }
}

@Composable
private fun TestScreen(
    onSmall: () -> Unit,
    onBig: () -> Unit,
    onLeak: () -> Unit,
    onFixLeak: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onSmall) { Text("Run SMALL save/load") }
        Button(onClick = onBig) { Text("Run BIG save/load") }
        Button(onClick = onLeak) { Text("Simulate Leak (Activity)") }
        Button(onClick = onFixLeak) { Text("Fix Leak (release)") }
    }
}
