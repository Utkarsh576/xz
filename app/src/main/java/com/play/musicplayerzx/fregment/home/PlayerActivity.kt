package com.play.musicplayerzx.fregment.home



import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R
import com.play.musicplayerzx.databinding.ActivityPlayerBinding
import com.play.musicplayerzx.services.MusicService
import com.play.musicplayerzx.setSongPosition


class PlayerActivity : AppCompatActivity(),ServiceConnection {

    companion object {
        lateinit var songList: List<AudioFile>
        var currentPosition: Int = 0
        var isPlaying: Boolean = false
        var handler: Handler = Handler()
        var runnable: Runnable? = null
       // var mediaPlayer:MediaPlayer?=null
        var musicService:MusicService?=null


        @Suppress("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)


        initializeLayout()


        binding.imageButtonprev.setOnClickListener {
            prevnextsong(false)
        }

        binding.imageButton2playpause.setOnClickListener {
            togglePlayPause()
        }

        binding.imageButton3next.setOnClickListener {
            prevnextsong(true)
        }


        musicService?.let {
            it.mediaPlayer?.setOnCompletionListener {
                // Automatically move to the next song when the current one finishes
                prevnextsong(true)
            }
        }

        createMediaPlayer()


    }

    private fun setLayoutUI() {
        Glide.with(this)
            .load(songList[currentPosition].albumArtUri)
            .placeholder(R.drawable.musicplayer)
            .error(R.drawable.musicplayer)
            .into(binding.audioAlbum)
        binding.songName.text = songList[currentPosition].title
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
         musicService!!.mediaPlayer!!.setDataSource(songList[currentPosition].path)
            musicService!!.mediaPlayer!!.prepare()
          musicService!!.mediaPlayer!!.start()
            isPlaying = true
            musicService!!.showNotification(R.drawable.ic_pause)

            updatePlayPauseButton()
            updateSeekBar()

        } catch (e: Exception) {
            return  e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    /*private fun datalistset(){
        currentPosition = intent.getIntExtra("position", 0)
        songList = MainActivity.audioList

        intent.getIntExtra("position", 0)
        val audioListf = intent.getSerializableExtra("audioListf") as? ArrayList<AudioFile>

        intent.getIntExtra("position", 0)
        val audioListR = intent.getSerializableExtra("audioListR") as? ArrayList<AudioFile>

        val position = intent.getIntExtra("position", -1)
        val audioListA = intent.getParcelableArrayListExtra<AudioFile>("audioListA")

        if (position != -1 && audioListA != null && position < audioListA.size) {
            val selectedAudio = audioListA[position]
            // Proceed with the selected audio file
        } else {
            Toast.makeText(this, "Invalid position or audio list", Toast.LENGTH_SHORT).show()
            finish() // Finish the activity if the position or audio list is invalid
        }

    }*/
    private fun datalistset(intent: Intent) {
        val position = intent.getIntExtra("position", 0) // Default position is 0

        // Check if the intent contains audio list for song list fragment
        val audioList = when {
            intent.hasExtra("audioListf") -> intent.getParcelableArrayListExtra<AudioFile>("audioListf")
            intent.hasExtra("audioListA") -> intent.getParcelableArrayListExtra<AudioFile>("audioListA")
            intent.hasExtra("audioListR") -> intent.getSerializableExtra("audioListR") as? ArrayList<AudioFile>
            else -> null
        }

        // Set current position and song list
        currentPosition = position
        songList = audioList ?: MainActivity.audioList
        //songList =  MainActivity.audioList
    }


    private fun initializeLayout() {

        datalistset(intent)
        //songList =  MainActivity.audioList
        setLayoutUI()
    }

    private fun updateSeekBar() {
        runnable = Runnable {
           musicService!!.mediaPlayer?.let {
                binding.seekBar.max = it.duration
                binding.seekBar.progress = it.currentPosition
                //binding.currentTimeTextView.text = formatDuration(it.currentPosition)
            }
            handler.postDelayed(runnable!!, 1000)
        }
        handler.postDelayed(runnable!!, 0)
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pauseSong()
        } else {
            resumeSong()
        }
    }

    private fun pauseSong() {
        musicService!!.mediaPlayer!!.pause()
        musicService!!.showNotification(R.drawable.ic_play)
        isPlaying = false
        updatePlayPauseButton()
    }

    private fun resumeSong() {
        musicService!!.mediaPlayer!!.start()
        musicService!!.showNotification(R.drawable.ic_pause)
        isPlaying = true
        updatePlayPauseButton()
        updateSeekBar()
    }

    private fun updatePlayPauseButton() {
        val imageResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        binding.imageButton2playpause.setImageResource(imageResource)
    }

    private fun prevnextsong(increment: Boolean) {
        if (increment) {
            setSongPosition(true)
            ui()
        } else {
            setSongPosition(false)
            ui()
        }
    }/*
    fun setSongPosition(increment:Boolean){

        if (increment) {
            PlayerActivity.currentPosition++
            if (PlayerActivity.currentPosition >= PlayerActivity.songList.size) {
                PlayerActivity.currentPosition = 0
            }
        } else {
            PlayerActivity.currentPosition--
            if (PlayerActivity.currentPosition < 0) {
                PlayerActivity.currentPosition = PlayerActivity.songList.size - 1
            }
        }
    }*/



    private fun ui() {
        createMediaPlayer()
        setLayoutUI()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MusicBinder
        musicService = binder.getService()
        createMediaPlayer()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }


}
