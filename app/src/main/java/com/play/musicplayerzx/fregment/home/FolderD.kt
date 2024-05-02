package com.play.musicplayerzx.fregment.home



import android.content.ContentUris
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
import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.AudioFolder
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.MainActivity.Companion.audioFolderList
import com.play.musicplayerzx.R


class FolderD : AppCompatActivity() {
    private val audioListf = ArrayList<AudioFile>()
    private lateinit var folderDAdapter: FolderDAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_folder_d)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val position = intent.getIntExtra("position", -1)
        val audioFolderList = MainActivity.audioFolderList

        val recyclerView: RecyclerView = findViewById(R.id.folderDRv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        folderDAdapter = FolderDAdapter(this, audioListf) { position ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("audioListf", audioListf)
            startActivity(intent)
        }
        recyclerView.adapter = folderDAdapter

        if (position != -1 && audioFolderList != null && position < audioFolderList.size) {
            val selectedAudioFolder = audioFolderList[position].folderName
            loadAudioFiles(selectedAudioFolder)
        } else {
            Toast.makeText(this, "Invalid position or artist list", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadAudioFiles(selectedAudioFolder: String) {
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
        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%$selectedAudioFolder%")
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
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )


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
                audioListf.add(audioFile)



            }
        }
        folderDAdapter.notifyDataSetChanged()
    }
}
