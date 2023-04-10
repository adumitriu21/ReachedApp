package com.example.reachedapp.interfaces

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface OnGoogleAuthListener {
    fun onGoogleAuthStart()
    fun onGoogleAuthSuccess(account: GoogleSignInAccount)
    fun onGoogleAuthError()
    fun onGoogleSignOutSuccess()
    fun onGoogleSignOutError()
}