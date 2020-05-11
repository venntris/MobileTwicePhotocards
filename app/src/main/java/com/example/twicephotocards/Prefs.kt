package com.example.twicephotocards

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "com.example.twicephotocards.prefs"
    val TOKEN = "apiToken"
    val USERNAME = "username"
    val PASSWORD = "password"
    val BASEURL = "baseUrl"
    val EMPTY_STRING = ""
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var apiToken: String?
        get() = prefs.getString(TOKEN, EMPTY_STRING)
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    var userName: String?
        get() = prefs.getString(USERNAME, EMPTY_STRING)
        set(value) = prefs.edit().putString(USERNAME, value).apply()

    var password: String?
        get() = prefs.getString(PASSWORD, EMPTY_STRING)
        set(value) = prefs.edit().putString(PASSWORD, value).apply()

    var baseUrl: String?
        get() = prefs.getString(BASEURL, EMPTY_STRING)
        set(value) = prefs.edit().putString(BASEURL, value).apply()
}