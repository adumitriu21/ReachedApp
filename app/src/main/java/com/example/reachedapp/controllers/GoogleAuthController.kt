package com.example.reachedapp.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reachedapp.MainActivity
import com.example.reachedapp.interfaces.OnGoogleAuthListener
import com.example.reachedapp.util.RC_SIGN_IN
import com.example.reachedapp.util.Session
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class GoogleAuthController(private val appContext: Context, private val listener: OnGoogleAuthListener) {

    private var gsc: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(appContext, gso)
    }

    fun signIn(fragment: Fragment) {
        listener?.onGoogleAuthStart()
        val signInIntent = gsc.signInIntent
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut(activity: Activity) {
        gsc.signOut().addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                Session.endUserSession(activity)
                activity.finish()
                activity.startActivity(Intent(activity, MainActivity::class.java))
                listener.onGoogleSignOutSuccess()
            } else {
                listener.onGoogleSignOutError()
            }
        }
    }

    fun handleSignInTask(completedTask: Task<GoogleSignInAccount>) {
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

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInTask(task)
        }
    }
}
