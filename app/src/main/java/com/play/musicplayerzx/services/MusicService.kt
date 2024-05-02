package com.play.musicplayerzx.services






import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat

import androidx.core.app.NotificationCompat



class MusicService : Service()
{
    private val binder = MusicBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    lateinit var audioManager: AudioManager

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My music")
        return binder
    }
/*
    fun showNotification(playPausebtn: Int) {
        // Create intent for previous action
        val prevIntent = Intent(baseContext, NotificationActionReceiver::class.java).setAction(AppClass.PREV)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent = Intent(baseContext, NotificationActionReceiver::class.java).setAction(AppClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(baseContext, NotificationActionReceiver::class.java).setAction(AppClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent = Intent(baseContext, NotificationActionReceiver::class.java).setAction(AppClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val imgArt = PlayerActivity.songList[PlayerActivity.currentPosition].path?.let {
            getImageArt(
                it
            )
        }
        val img = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.musicplayer)
        }

        val notification = NotificationCompat.Builder(this, AppClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.songList[PlayerActivity.currentPosition].title)
            .setContentText(PlayerActivity.songList[PlayerActivity.currentPosition].artist)
            .setSmallIcon(R.drawable.musicalnote)
            .setLargeIcon(img)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_prev, "prev", prevPendingIntent)
            .addAction(playPausebtn, "play", playPendingIntent)
            .addAction(R.drawable.ic_next, "next", nextPendingIntent)
            .addAction(R.drawable.ic_exit, "exit", exitPendingIntent)
            .build()

        startForeground(1, notification)
    }

    fun getImageArt(path: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        return retriever.embeddedPicture
    }



    fun startForeground(b: Boolean) {

    }*/

}

