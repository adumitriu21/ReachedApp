package com.example.reachedapp.Controllers

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.reachedapp.Models.User
import com.example.reachedapp.Util.MyFirebaseMessagingService
import com.example.reachedapp.Util.Session
import com.google.firebase.auth.FirebaseAuth

interface LoginCallback {
    fun onLoginSuccess(user: User)
    fun onLoginError()
}

class AuthController(private val auth: FirebaseAuth) {
    fun authenticateUser(context: Context, user: User, callback: LoginCallback) {
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
                    callback.onLoginSuccess(user)
                } else {
                    // Login failed, call onLoginError callback
                    callback.onLoginError()
                }
            }
    }

}
