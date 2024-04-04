package com.butul0ve.urbanslang.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.butul0ve.urbanslang.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.atomic.AtomicInteger
import com.butul0ve.urbanslang.R
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat

private const val NOTIFICATION_CHANNEL_ID = "25007"

class UrbanSlangMessagingService : FirebaseMessagingService() {

    private val notificationId = AtomicInteger(0)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(remoteMessage)
    }

    private fun showNotification(message: RemoteMessage?) {
        if (message == null) return

        val notification = message.notification

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setColor(ContextCompat.getColor(this, R.color.colorPinky))
            .setContentTitle(notification?.title)
            .setContentText(notification?.body)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "name",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
            notificationManager.notify(notificationId.getAndIncrement(), builder.build())
        } else {
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(notificationId.getAndIncrement(), builder.build())
        }
    }
}