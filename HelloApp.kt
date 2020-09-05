package com.example.helllo1

import android.app.Application
import com.facebook.stetho.Stetho

class HelloApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}