package com.example.reachedapp.Util

import android.util.Log
import com.example.reachedapp.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.reachedapp.Models.UserRole
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Context
import com.google.firebase.messaging.FirebaseMessaging

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private fun determineUserRoleFromUserId(userId: String): UserRole {
        // Logic to determine the user role based on user ID
        // For example:
        return when {
            userId.startsWith("P") -> UserRole.Parent
            userId.startsWith("T") -> UserRole.Teacher
            userId.startsWith("A") -> UserRole.Admin
            else -> UserRole.Default
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
        @JvmStatic
        fun sendRegistrationToServer(context: android.content.Context, user: User) {
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.reference.child(user.userRole.toString())

            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                if (token != null) {
                    usersRef.child(user.userId).child("deviceToken").setValue(token)
                        .addOnSuccessListener {
                            Log.d(TAG, "Device token updated successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "Failed to update device token", e)
                        }
                }
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
    }






}
