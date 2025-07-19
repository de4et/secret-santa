package com.example.secret_santa.storage.event

import android.content.Context
import com.example.secret_santa.model.event.Event
import java.io.File
import com.google.gson.Gson
import java.util.UUID

object EventStorage {
    private lateinit var appContext: Context
    private lateinit var fileName: String
    private val gson = Gson()


    fun init(context: Context, fileName: String) {
        appContext = context.applicationContext
        this.fileName = fileName
    }

    private fun saveAll(events: List<Event>) {
        File(appContext.filesDir, fileName).writeText(gson.toJson(events))
    }

    private fun loadAll(): List<Event> {
        val file = File(appContext.filesDir, fileName)
        return if (file.exists()) {
            gson.fromJson(file.readText(), Array<Event>::class.java).toList()
        } else emptyList()
    }

    fun getAll(): List<Event> = loadAll()
    fun getById(id: String): Event? = loadAll().find { it.id == id }

    fun add(name: String, date: String, participants: List<Int>) {
        val l = loadAll()
        saveAll(l + Event( name = name,
            id = UUID.randomUUID().toString(),
            dateStart = date,
            isLocked = false,
            participants = participants
        ))
    }

    fun update(updatedEvent: Event) {
        saveAll(loadAll().map { if (it.id == updatedEvent.id) updatedEvent else it })
    }

    fun delete(id: String) {
        saveAll(loadAll().filter { it.id != id })
    }
}
