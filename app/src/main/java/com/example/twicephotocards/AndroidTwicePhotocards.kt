package com.example.twicephotocards

import android.app.Application
import android.content.SharedPreferences
import com.example.twicephotocards.Prefs

val prefs: Prefs by lazy {
    AndroidTwicePhotocards.prefs!!
}

class AndroidTwicePhotocards : Application() {
    var apiToken: String? = null
    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}