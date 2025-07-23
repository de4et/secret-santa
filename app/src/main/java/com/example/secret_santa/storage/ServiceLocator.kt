package com.example.secret_santa.storage

import android.content.Context
import com.example.secret_santa.service.event.EventService
import com.example.secret_santa.storage.event.EventStorage
import com.example.secret_santa.storage.user.UserStorage

object ServiceLocator {
    private lateinit var appContext: Context

    val eventStorage: EventStorage by lazy {
        EventStorage(appContext, "events.json")
    }

    val userStorage: UserStorage by lazy {
        UserStorage(appContext, "users.json")
    }

    val eventService: EventService by lazy {
        EventService()
    }

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
