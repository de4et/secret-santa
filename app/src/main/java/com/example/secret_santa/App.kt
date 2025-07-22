package com.example.secret_santa

import android.app.Application
import com.example.secret_santa.storage.ServiceLocator

class App:  Application() {
    private val serviceLocator = ServiceLocator
    override fun onCreate() {
        super.onCreate()
        serviceLocator.init(this)
    }
}