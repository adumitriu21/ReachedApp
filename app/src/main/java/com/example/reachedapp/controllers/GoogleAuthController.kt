package com.example.reachedapp.controllers

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reachedapp.interfaces.OnGoogleAuthListener
import com.example.reachedapp.util.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class GoogleAuthController(private val context: Context) {

    private var gsc: GoogleSignInClient
    private lateinit var listener: OnGoogleAuthListener

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(context, gso)
    }

    fun setOnGoogleAuthListener(listener: OnGoogleAuthListener) {
        this.listener = listener
    }


    fun signIn(fragment: Fragment) {
        listener?.onGoogleAuthStart()
        val signInIntent = gsc.signInIntent
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    fun signOut() {
        gsc.signOut().addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                listener.onGoogleSignOutSuccess()
            } else {
                listener.onGoogleSignOutError()
            }
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                listener?.onGoogleAuthSuccess(account)
            } else {
                listener?.onGoogleAuthError()
            }
        } catch (e: ApiException) {
            listener?.onGoogleAuthError()
        }
    }

}