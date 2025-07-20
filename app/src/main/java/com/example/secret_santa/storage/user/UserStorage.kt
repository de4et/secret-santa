package com.example.secret_santa.storage.user

import android.content.Context
import com.example.secret_santa.model.user.User
import java.io.File
import com.google.gson.Gson
import java.util.UUID

class UserStorage(context: Context, fileName: String) {
    private lateinit var appContext: Context
    private lateinit var fileName: String
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

    fun add(name: String, wishes: String?, pathToImage: String?) {
        val l = loadAll()
        saveAll(l + User(
            name = name,
            id = UUID.randomUUID().toString(),
            pathToImage = pathToImage,
            wishes = wishes
        ))
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
