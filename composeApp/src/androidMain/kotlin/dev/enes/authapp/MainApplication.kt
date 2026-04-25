package dev.enes.authapp

import android.app.Application
import dev.enes.authapp.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
