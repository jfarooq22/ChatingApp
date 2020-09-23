package com.example.chatingapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import com.example.chatingapp.R
import com.example.chatingapp.models.ChatMessage
import com.example.chatingapp.models.User
import com.example.chatingapp.views.LatestMessagesActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private lateinit var title:String;
    private lateinit var message:String;
    override fun onMessageReceived(@NonNull remoteMessage:RemoteMessage){
        super.onMessageReceived(remoteMessage)
        title = remoteMessage.getData().get("Title").toString()
        message = remoteMessage.getData().get("Message").toString()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChatingApp"
            val descriptionText = "Message Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("ChatingAppNotifications", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system

            manager!!.createNotificationChannel(channel)
        }

        val builder =NotificationCompat.Builder(applicationContext)
            .setSmallIcon(R.mipmap.messaginglogo_round)
            .setContentTitle(title)
            .setContentText(message)
            .setChannelId("ChatingAppNotifications")

        manager.notify(0, builder.build())
    }

//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "ChatingApp"
//            val descriptionText = "Message Notification"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel("ChatingApp", name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager!!.createNotificationChannel(channel)
//        }
//
//    }
}