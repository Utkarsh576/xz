package com.play.musicplayerzx.services


import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class AppClass: Application() {
    companion object{
        const val CHANNEL_ID="name"
        const val PLAY="play"
        const val PREV="prev"
        const val NEXT="next"
        const val EXIT="exit"
    }
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "now play audio",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "this is important channel"
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
