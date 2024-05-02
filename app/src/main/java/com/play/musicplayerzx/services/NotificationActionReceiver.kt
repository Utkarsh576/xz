package com.play.musicplayerzx.services






import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bumptech.glide.Glide
import com.play.musicplayerzx.R
import com.play.musicplayerzx.fregment.home.PlayerActivity
import com.play.musicplayerzx.setSongPosition


import kotlin.system.exitProcess

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            AppClass.PREV -> {
                if (context != null) {
                    nextMusic(increment = false, context)
                }
            }
            AppClass.PLAY -> {
                if (PlayerActivity.isPlaying) pauseMusic() else playMusic()
            }
            AppClass.NEXT -> {
                if (context != null) {
                    nextMusic(increment = true, context)
                }
            }
            AppClass.EXIT -> {
                PlayerActivity.musicService?.stopForeground(true)
                PlayerActivity.musicService!!.mediaPlayer!!.release()
                PlayerActivity.musicService = null

                exitProcess(1) // This will close the app
            }
        }
    }

    private fun playMusic() {
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService?.mediaPlayer?.start()
        PlayerActivity.musicService?.showNotification(R.drawable.ic_pause)
        PlayerActivity.binding.imageButton2playpause.setImageResource(R.drawable.ic_pause)
    }

    private fun pauseMusic() {
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService?.mediaPlayer?.pause()
        PlayerActivity.musicService?.showNotification(R.drawable.ic_play)
        PlayerActivity.binding.imageButton2playpause.setImageResource(R.drawable.ic_play)
    }


    private fun nextMusic(increment: Boolean, context: Context) {
        setSongPosition(increment)

        try {
            PlayerActivity.musicService?.mediaPlayer?.reset()
            PlayerActivity.musicService?.mediaPlayer?.setDataSource(PlayerActivity.songList[PlayerActivity.currentPosition].path)
            PlayerActivity.musicService?.mediaPlayer?.prepare()
            PlayerActivity.musicService?.showNotification(R.drawable.ic_pause)
            playMusic()

            PlayerActivity.binding.songName.text = PlayerActivity.songList[PlayerActivity.currentPosition].title

            if (context != null) {
                Glide.with(context)
                    .load(PlayerActivity.songList[PlayerActivity.currentPosition].albumArtUri)
                    .placeholder(R.drawable.musicplayer)
                    .error(R.drawable.musicplayer)
                    .into(PlayerActivity.binding.audioAlbum)
            }

        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error preparing media player: ${e.message}")
        }
    }
}

