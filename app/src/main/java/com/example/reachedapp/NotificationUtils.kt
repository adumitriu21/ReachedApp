package com.example.reachedapp


import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

private val COMMENT_NOTIFICATION_ID = 100;
fun NotificationManager.sendNotification(
        title: String,
        message: String,
        channel: String,
        applicationContext: Context
) {

    //TODO("Create Notification")
    val builder = NotificationCompat.Builder(
            applicationContext,
            channel
    )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

    when(channel){
        applicationContext.getString(R.string.comment_notification_channel_id) -> notify(
                COMMENT_NOTIFICATION_ID, builder.build())


    }

}

fun NotificationManager.cancelNotification() = cancelAll()