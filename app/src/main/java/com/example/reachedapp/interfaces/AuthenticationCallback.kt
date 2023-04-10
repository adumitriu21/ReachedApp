package com.example.reachedapp.interfaces

import com.example.reachedapp.models.User

interface AuthenticationCallback {
    fun onAuthenticationSuccess(user: User)
    fun onAuthenticationFailure()
}