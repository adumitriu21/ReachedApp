package com.example.reachedapp.util

import android.content.Context
import com.example.reachedapp.models.User
import com.google.gson.Gson
import java.util.*

object Session {

    private const val SESSION_PREFERENCES = "com.example.reachedapp.SESSION_PREFERENCES"
    private const val SESSION_TOKEN = "com.example.reachedapp.SESSION_TOKEN"
    private const val SESSION_USER = "com.example.reachedapp.SESSION_USER"
    private const val SESSION_EXPIRY_TIME = "com.example.reachedapp.SESSION_EXPIRY_TIME"

    fun startUserSession(context: Context, expiresIn: Int) {
        val calendar = Calendar.getInstance()
        val userLoggedInTime = calendar.time
        calendar.time = userLoggedInTime
        calendar.add(Calendar.SECOND, expiresIn)
        val expiryTime = calendar.time
        val tokenSharedPreferences = context.getSharedPreferences(SESSION_PREFERENCES, 0)
        val editor = tokenSharedPreferences.edit()
        editor.putLong(SESSION_EXPIRY_TIME, expiryTime.time)
        editor.apply()
    }

    fun isSessionActive(currentTime: Date,context: Context) : Boolean {
        val sessionExpiresAt = Date(getExpiryDateFromPreferences(context)!!)
        return !currentTime.after(sessionExpiresAt)
    }

    private fun getExpiryDateFromPreferences(context: Context) : Long? {
        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getLong(SESSION_EXPIRY_TIME, 0)
    }

    fun storeUserToken(context: Context, token: String) {
        val tokenEditor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        tokenEditor.putString(SESSION_TOKEN, token)
        tokenEditor.apply()
    }

    fun getUserToken(context: Context) : String? {
        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getString(SESSION_TOKEN, "")
    }

    fun storeUser(context: Context, user: User) {
        val userInSession = Gson().toJson(user)
        val tokenEditor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        tokenEditor.putString(SESSION_USER, userInSession)
        tokenEditor.apply()
    }

    fun getUser(context: Context) : User? {
        val storedUser = context.getSharedPreferences(SESSION_PREFERENCES, 0).getString(SESSION_USER, "")
        return  Gson().fromJson<User>(storedUser,  User::class.java)
    }

    fun endUserSession(context: Context) {
        clearStoredData(context)
    }

    private fun clearStoredData(context: Context) {
        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.clear()
        editor.apply()
    }
}