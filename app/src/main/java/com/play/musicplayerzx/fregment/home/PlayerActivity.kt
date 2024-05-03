package com.play.musicplayerzx.fregment.home
import androidx.palette.graphics.Palette
import android.graphics.Bitmap
import com.bumptech.glide.request.transition.Transition

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R
import com.play.musicplayerzx.databinding.ActivityPlayerBinding
import com.play.musicplayerzx.services.MusicService
class PlayerActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var audioManager: AudioManager
    private lateinit var phoneStateListener: PhoneStateListener

    companion object {
        lateinit var songList: List<AudioFile>
        var currentPosition: Int = 0
        var isPlaying: Boolean = false
        var handler: Handler = Handler()
        var runnable: Runnable? = null
        var musicService: MusicService? = null

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

        // Initialize AudioManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // Initialize layout
        initializeLayout()

        // Set up click listeners
        binding.imageButtonprev.setOnClickListener { prevNextSong(false) }
        binding.imageButton2playpause.setOnClickListener { togglePlayPause() }
        binding.imageButton3next.setOnClickListener { prevNextSong(true) }

        // Initialize PhoneStateListener
        initPhoneStateListener()

        // Bind to MusicService
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
        startService(intent)




    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister PhoneStateListener
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)

        // Remove callbacks from the handler
        runnable?.let { handler.removeCallbacks(it) }
    }

    private fun initializeLayout() {
        dataListSet(intent)
        setLayoutUI()
    }



    private fun updateSeekBar() {
        runnable = Runnable {
            musicService?.mediaPlayer?.let {
                binding.seekBar.max = it.duration
                binding.seekBar.progress = it.currentPosition
            }
            handler.postDelayed(runnable!!, 1000)
        }
        handler.postDelayed(runnable!!, 0)
    }

    private fun setLayoutUI() {
        Glide.with(this)
            .load(songList[currentPosition].albumArtUri)
            .placeholder(R.drawable.musicplayer)
            .error(R.drawable.musicplayer)
            .into(binding.audioAlbum)
        binding.songName.text = songList[currentPosition].title
    }

    private fun dataListSet(intent: Intent) {
        val position = intent.getIntExtra("position", 0)
        val audioList = when {
            intent.hasExtra("audioListf") -> intent.getParcelableArrayListExtra<AudioFile>("audioListf")
            intent.hasExtra("audioListA") -> intent.getParcelableArrayListExtra<AudioFile>("audioListA")
            intent.hasExtra("audioListR") -> intent.getSerializableExtra("audioListR") as? ArrayList<AudioFile>
            else -> null
        }
        currentPosition = position
        songList = audioList ?: MainActivity.audioList
    }

    private fun initPhoneStateListener() {
        // Register PhoneStateListener
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING, TelephonyManager.CALL_STATE_OFFHOOK -> {
                        pauseSong()
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        resumeSong()
                    }
                }
            }
        }
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pauseSong()
        } else {
            resumeSong()
        }
    }

    private fun pauseSong() {
        musicService?.mediaPlayer?.pause()
        isPlaying = false
        updatePlayPauseButton()
        musicService?.showNotification(R.drawable.ic_play)
    }

    private fun resumeSong() {
        musicService?.mediaPlayer?.start()
        isPlaying = true
        updatePlayPauseButton()
        updateSeekBar()
        musicService?.showNotification(R.drawable.ic_pause)
    }

    private fun updatePlayPauseButton() {
        val imageResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        binding.imageButton2playpause.setImageResource(imageResource)
    }

    private fun prevNextSong(increment: Boolean) {
        if (increment) {
            setSongPosition(true)
            updateUI()
        } else {
            setSongPosition(false)
            updateUI()
        }
    }

    private fun setSongPosition(increment: Boolean) {
        if (increment) {
            currentPosition++
            if (currentPosition >= songList.size) {
                currentPosition = 0
            }
        } else {
            currentPosition--
            if (currentPosition < 0) {
                currentPosition = songList.size - 1
            }
        }
    }

    private fun updateUI() {
        createMediaPlayer()
        setLayoutUI()
    }

    private fun createMediaPlayer() {
        try {
            if (musicService?.mediaPlayer == null) musicService?.mediaPlayer = MediaPlayer()
            musicService?.mediaPlayer?.reset()
            musicService?.mediaPlayer?.setDataSource(songList[currentPosition].path)
            musicService?.mediaPlayer?.prepare()
            musicService?.mediaPlayer?.start()
            isPlaying = true
            musicService?.showNotification(R.drawable.ic_pause)
            updatePlayPauseButton()
            updateSeekBar()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as? MusicService.MusicBinder
        musicService = binder?.getService()
        createMediaPlayer()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}
