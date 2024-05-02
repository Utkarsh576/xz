package com.play.musicplayerzx.fregment.home

import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ArtistD : AppCompatActivity() {
    private lateinit var artistDAdapter: ArtistDAdapter
    private val audioListR = ArrayList<AudioFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist_d)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val artistRecyclerView: RecyclerView = findViewById(R.id.artistDrv)
        artistDAdapter = ArtistDAdapter(audioListR) { position ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("audioListR", audioListR)
            startActivity(intent)
        }
        artistRecyclerView.adapter = artistDAdapter
        artistRecyclerView.layoutManager = LinearLayoutManager(this)

        val position = intent.getIntExtra("position", -1)
        val artistList = MainActivity.artistList

        if (position != -1 && artistList != null && position < artistList.size) {
            val selectedArtist = artistList[position]
            loadAudioFiles(selectedArtist)
        } else {
            Toast.makeText(this, "Invalid position or artist list", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadAudioFiles(selectedArtist: String) {
        val audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val audioProjection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA
        )
        val selection = "${MediaStore.Audio.Media.ARTIST}=?"
        val selectionArgs = arrayOf(selectedArtist)
        val audioCursor = contentResolver.query(
            audioUri,
            audioProjection,
            selection,
            selectionArgs,
            null
        )

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
                val albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")

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

                audioListR.add(audioFile)

                if (!MainActivity.artistList.contains(artist)) {
                    MainActivity.artistList.add(artist)
                }
            }
        }
        artistDAdapter.notifyDataSetChanged()
    }
}
