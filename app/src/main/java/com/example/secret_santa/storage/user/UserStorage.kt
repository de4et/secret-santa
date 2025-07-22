package com.example.secret_santa.storage.user

import android.content.Context
import com.example.secret_santa.model.user.User
import java.io.File
import com.google.gson.Gson
import java.util.UUID

class UserStorage(private val appContext: Context, private val fileName: String) {
    private val gson = Gson()

    private fun saveAll(events: List<User>) {
        File(appContext.filesDir, fileName).writeText(gson.toJson(events))
    }

    private fun loadAll(): List<User> {
        val file = File(appContext.filesDir, fileName)
        return if (file.exists()) {
            gson.fromJson(file.readText(), Array<User>::class.java).toList()
        } else emptyList()
    }

    fun getAll(): List<User> = loadAll()
    fun getById(id: String): User? = loadAll().find { it.id == id }

    fun add(name: String, wishes: String?, pathToImage: String?): String {
        val l = loadAll()
        val id = UUID.randomUUID().toString()
        saveAll(l + User(
            name = name,
            id = id,
            pathToImage = pathToImage,
            wishes = wishes
        ))
        return id
    }

    fun update(updatedUser: User) {
        saveAll(loadAll().map { if (it.id == updatedUser.id) updatedUser else it })
    }

    fun delete(id: String) {
        saveAll(loadAll().filter { it.id != id })
    }

    fun deleteAll() {
        saveAll(emptyList())
    }
}
