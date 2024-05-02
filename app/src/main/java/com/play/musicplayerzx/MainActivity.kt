package com.play.musicplayerzx
import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.play.musicplayerzx.databinding.ActivityMainBinding
import com.play.musicplayerzx.fregment.HomeFragment
import com.play.musicplayerzx.fregment.SettingsFragment

class MainActivity : AppCompatActivity() {
    private var backPressedOnce = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,0)
            insets
        }
        checkPermissions()

        binding.bottomNav.setOnNavigationItemSelectedListener(navListener)

        // Set the default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment = when (item.itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_settings -> SettingsFragment()
            else -> HomeFragment() // Default to HomeFragment
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()
        true
    }
    private fun checkPermissions() {
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = arrayListOf<String>()

        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSIONS_REQUEST_CODE
            )
        } else {
            // Permissions already granted, proceed with loading audio files
            loadAudioFiles()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Permissions granted, proceed with loading audio and video files
                    loadAudioFiles()
                    //loadVideoFiles()
                }

                else{
                    checkPermissions()

                }
            }
        }

    }
    private fun loadAudioFiles() {
        val audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val audioProjection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val audioCursor = contentResolver.query(audioUri, audioProjection, null, null, null)

        audioCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val duration = cursor.getLong(durationColumn)
                val size = cursor.getLong(sizeColumn)
                val path = cursor.getString(pathColumn)
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )
                val filePath = path.substringBeforeLast('/')
                val folderName = filePath.substringAfterLast('/')

                val existingAlbum = albumList.find { info ->
                    info.album == album && info.albumId == albumId
                }
                if (existingAlbum == null) {
                    // If not, add it along with the art URI
                    val albumInfo = AlbumInfo(albumId, album, albumArtUri)
                    albumList.add(albumInfo)
                }

                val audioFile = AudioFile(
                    id,
                    title,
                    artist,
                    album,
                    albumId,
                    duration,
                    size,
                    path,
                    albumArtUri
                )
                audioList.add(audioFile)
                val audioFolder = AudioFolder(folderName)
                if (!audioFolderList.contains(audioFolder)) {
                    audioFolderList.add(audioFolder)
                }

                if (!artistList.contains(artist)) {
                    artistList.add(artist)
                }

            }
        }
    }
    companion object {

        private const val PERMISSIONS_REQUEST_CODE = 100
        val audioList = ArrayList<AudioFile>() // ArrayList to store audio files
        //val videoList = ArrayList<VideoFile>() // ArrayList to store video files
        val audioFolderList = ArrayList<AudioFolder>()
        val albumList = ArrayList<AlbumInfo>()
        val artistList = ArrayList<String>()
        /*val folderList = ArrayList<VideoFolder>()

        */
    }
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            return showExitConfirmationDialog()
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

        // Reset backPressedOnce after a delay
        android.os.Handler().postDelayed(
            { backPressedOnce = false },
            2000 // Time interval in milliseconds
        )
    }
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Exit") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}
