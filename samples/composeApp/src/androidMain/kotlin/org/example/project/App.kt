package org.example.project

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ActivityManager.init(this)
    }
}