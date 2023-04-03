package com.example.reachedapp.Util

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reachedapp.Models.User
import com.example.reachedapp.Models.UserRole
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONStringer


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
        super.onMessageReceived(remoteMessage)

        // Extract the notification payload
        val notification = remoteMessage.notification
        val title = notification?.title
        val body = notification?.body

        // Create the notification channel
        createNotificationChannel()
        // Display the notification to the user
        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        // Create a notification channel for Android 8.0 (API level 26) and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "Notification Channel for Reached App"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Prepare the notification
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to high

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(/* notification id */ 0, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("your_notification_channel_id", "Channel Name", importance).apply {
                description = "Channel Description"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}
