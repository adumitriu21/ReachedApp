package com.example.reachedapp.controllers

import android.content.Context
import com.example.reachedapp.interfaces.AuthenticationCallback
import com.example.reachedapp.models.User
import com.example.reachedapp.util.MyFirebaseMessagingService
import com.example.reachedapp.util.Session
import com.google.firebase.auth.FirebaseAuth

class AuthController(private val auth: FirebaseAuth) {
    fun authenticateUser(context: Context, user: User, callback: AuthenticationCallback) {
        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Session.startUserSession(context, 60)
                    task.result?.user?.getIdToken(true)
                        ?.addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                Session.storeUserToken(
                                    context,
                                    authTask.result?.token.toString()
                                )
                            }
                        }
                    // Store user in session
                    Session.storeUser(context, user)

                    // Update the device token in the database
                    MyFirebaseMessagingService.sendRegistrationToServer(context, user)

                    // Call onLoginSuccess callback
                    callback.onAuthenticationSuccess(user)
                } else {
                    // Login failed, call onLoginError callback
                    callback.onAuthenticationFailure()
                }
            }
    }

}
